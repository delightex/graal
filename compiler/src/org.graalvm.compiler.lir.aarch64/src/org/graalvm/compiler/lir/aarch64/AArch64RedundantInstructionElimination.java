/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2020, Arm Limited and affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.graalvm.compiler.lir.aarch64;

import static org.graalvm.compiler.lir.LIRValueUtil.asVariable;
import static org.graalvm.compiler.lir.LIRValueUtil.isVariable;

import java.util.ArrayList;
import java.util.Collections;

import org.graalvm.compiler.core.common.cfg.AbstractBlockBase;
import org.graalvm.compiler.debug.DebugContext;
import org.graalvm.compiler.debug.Indent;
import org.graalvm.compiler.lir.InstructionValueConsumer;
import org.graalvm.compiler.lir.LIR;
import org.graalvm.compiler.lir.LIRInstruction;
import org.graalvm.compiler.lir.VariableMap;
import org.graalvm.compiler.lir.gen.LIRGenerationResult;
import org.graalvm.compiler.lir.phases.PreAllocationOptimizationPhase;

import jdk.vm.ci.code.TargetDescription;
import jdk.vm.ci.meta.Value;

/**
 * This optimization tries to remove unused instructions. Currently it only removes the constant
 * load instructions. The unused constant mov may exist after match rules.
 */
public final class AArch64RedundantInstructionElimination extends PreAllocationOptimizationPhase {

    @Override
    protected void run(TargetDescription target, LIRGenerationResult lirGenRes, PreAllocationOptimizationContext context) {
        new Optimization(lirGenRes.getLIR()).apply();
    }

    private static final class Optimization {
        private final LIR lir;
        private final VariableMap<DefEntry> unUsedDefs;
        private final DebugContext debug;

        private Optimization(LIR lir) {
            this.lir = lir;
            this.debug = lir.getDebug();
            this.unUsedDefs = new VariableMap<>();
        }

        @SuppressWarnings("try")
        private void apply() {
            try (Indent indent = debug.logAndIndent("AArch64RedundantInstructionElimination")) {
                try (DebugContext.Scope s = debug.scope("CollectUnusedInstructions")) {
                    for (AbstractBlockBase<?> b : lir.getControlFlowGraph().getBlocks()) {
                        collectUnusedInBlock(b);
                    }

                    if (!unUsedDefs.isEmpty()) {
                        debug.log("Redundant instructions that will be removed later: %s", unUsedDefs);
                    }
                } catch (Throwable e) {
                    throw debug.handle(e);
                }

                try (DebugContext.Scope s = debug.scope("DeleteUnusedInstructions")) {
                    unUsedDefs.forEach(this::deleteInstruction);
                    // Remove unused instructions for each block.
                    for (AbstractBlockBase<?> b : lir.getControlFlowGraph().getBlocks()) {
                        removeUnusedInBlock(b);
                    }
                } catch (Throwable e) {
                    throw debug.handle(e);
                }
            }
        }

        /**
         * Collect unused instructions for a {@code block}.
         */
        private void collectUnusedInBlock(AbstractBlockBase<?> block) {
            InstructionValueConsumer defConsumer = (instruction, value, mode, flags) -> {
                if (isVariable(value) && isEligibleOp(instruction)) {
                    DefEntry def = new DefEntry(block, instruction, value);
                    unUsedDefs.put(asVariable(value), def);
                }
            };

            InstructionValueConsumer useConsumer = (instruction, value, mode, flags) -> {
                if (isVariable(value)) {
                    DefEntry def = unUsedDefs.get(asVariable(value));
                    if (def != null) {
                        assert def.value.equals(value);
                        // Remove the instruction that defined this value from the unused map. We
                        // assume that one instruction can only define a variable.
                        unUsedDefs.remove(asVariable(value));
                        assert !unUsedDefs.contains(d -> (d.instruction.equals(def.instruction)));
                    }
                }
            };

            int opId = 0;
            for (LIRInstruction inst : lir.getLIRforBlock(block)) {
                inst.setId(opId++);
                inst.visitEachOutput(defConsumer);
                inst.visitEachInput(useConsumer);
                inst.visitEachAlive(useConsumer);
                inst.visitEachTemp(useConsumer);
                inst.visitEachState(useConsumer);
            }
        }

        /**
         * Currently we only find the unused constant load instructions that can be safely removed.
         * If there are any other kinds of eligible instructions, please extend the condition here.
         */
        private static boolean isEligibleOp(LIRInstruction inst) {
            return inst.isLoadConstantOp();
        }

        /**
         * Remove unused instructions for a {@code block}.
         */
        private void removeUnusedInBlock(AbstractBlockBase<?> block) {
            boolean hasDead = false;
            ArrayList<LIRInstruction> instructions = lir.getLIRforBlock(block);
            for (LIRInstruction instruction : instructions) {
                if (instruction == null) {
                    hasDead = true;
                } else {
                    instruction.setId(-1);
                }
            }

            // delete unused instructions
            if (hasDead) {
                instructions.removeAll(Collections.singleton(null));
            }
        }

        // Set the instruction that will be deleted to null.
        private void deleteInstruction(DefEntry def) {
            AbstractBlockBase<?> block = def.block;
            LIRInstruction instruction = def.instruction;
            ArrayList<LIRInstruction> instructions = lir.getLIRforBlock(block);
            assert instructions.contains(instruction);
            debug.log("deleting instruction %s from block %s", instruction, block);
            instructions.set(instruction.id(), null);
        }
    }

    /**
     * Represents an output of an instruction.
     */
    private static class DefEntry {

        final AbstractBlockBase<?> block;
        final LIRInstruction instruction;
        final Value value;

        DefEntry(AbstractBlockBase<?> block, LIRInstruction instruction, Value value) {
            this.block = block;
            this.instruction = instruction;
            this.value = value;
            assert instruction != null && block != null && value != null;
        }

        @Override
        public String toString() {
            return instruction.toString() + " in block " + block.toString();
        }
    }
}
