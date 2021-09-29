/*
 * Copyright (c) 2021, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.svm.core.posix.darwin;

import static com.oracle.svm.core.RegisterDumper.dumpReg;

import org.graalvm.nativeimage.ImageSingletons;
import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.Platforms;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.word.Pointer;
import org.graalvm.word.PointerBase;
import org.graalvm.word.WordFactory;

import com.oracle.svm.core.RegisterDumper;
import com.oracle.svm.core.annotate.AutomaticFeature;
import com.oracle.svm.core.annotate.Uninterruptible;
import com.oracle.svm.core.log.Log;
import com.oracle.svm.core.posix.UContextRegisterDumper;
import com.oracle.svm.core.posix.headers.Signal;
import com.oracle.svm.core.posix.headers.Signal.DarwinGregsPointer;
import com.oracle.svm.core.posix.headers.Signal.ucontext_t;
import com.oracle.svm.core.util.VMError;

@Platforms({Platform.IOS_AARCH64.class})
@AutomaticFeature
class DarwinArmUContextRegisterDumperFeature implements Feature {
    public DarwinArmUContextRegisterDumperFeature() {
        System.out.println("DarwinArmUContextRegisterDumperFeature");
    }
    @Override
    public void afterRegistration(AfterRegistrationAccess access) {
        ImageSingletons.add(RegisterDumper.class, new DarwinArmUContextRegisterDumper());
    }
}

class DarwinArmUContextRegisterDumper implements UContextRegisterDumper {
    @Override
    public void dumpRegisters(Log log, ucontext_t uContext, boolean printLocationInfo, boolean allowJavaHeapAccess, boolean allowUnsafeOperations) {
        Signal.MContext64Arm sigcontext = uContext.uc_mcontext64_arm();
        DarwinGregsPointer regs = sigcontext.regs();
        dumpReg(log, "R0 ", regs.read(0), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R1 ", regs.read(1), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R2 ", regs.read(2), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R3 ", regs.read(3), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R4 ", regs.read(4), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R5 ", regs.read(5), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R6 ", regs.read(6), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R7 ", regs.read(7), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R8 ", regs.read(8), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R9 ", regs.read(9), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R10 ", regs.read(10), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R11 ", regs.read(11), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R12 ", regs.read(12), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R13 ", regs.read(13), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R14 ", regs.read(14), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R15 ", regs.read(15), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R16 ", regs.read(16), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R17 ", regs.read(17), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R18 ", regs.read(18), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R19 ", regs.read(19), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R20 ", regs.read(20), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R21 ", regs.read(21), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R22 ", regs.read(22), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R23 ", regs.read(23), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R24 ", regs.read(24), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R25 ", regs.read(25), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R26 ", regs.read(26), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R27 ", regs.read(27), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R28 ", regs.read(28), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "FP", ((Pointer) sigcontext).readLong(sigcontext.fp_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "LR", ((Pointer) sigcontext).readLong(sigcontext.lr_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "SP", ((Pointer) sigcontext).readLong(sigcontext.sp_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "PC", ((Pointer) sigcontext).readLong(sigcontext.pc_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "CPSR", ((Pointer) sigcontext).readLong(sigcontext.cpsr_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "PAD", ((Pointer) sigcontext).readLong(sigcontext.pad_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
    }

    @Override
    @Uninterruptible(reason = "Called from uninterruptible code", mayBeInlined = true)
    public PointerBase getHeapBase(ucontext_t uContext) {
        Signal.MContext64Arm sigcontext = uContext.uc_mcontext64_arm();
        return WordFactory.nullPointer();
    }

    @Override
    @Uninterruptible(reason = "Called from uninterruptible code", mayBeInlined = true)
    public PointerBase getThreadPointer(ucontext_t uContext) {
        Signal.MContext64Arm sigcontext = uContext.uc_mcontext64_arm();
        return WordFactory.nullPointer();
    }

    @Override
    public PointerBase getSP(ucontext_t uContext) {
        Signal.MContext64Arm sigcontext = uContext.uc_mcontext64_arm();
        return WordFactory.pointer(sigcontext.sp_offset());
    }

    @Override
    public PointerBase getIP(ucontext_t uContext) {
        Signal.MContext64Arm sigcontext = uContext.uc_mcontext64_arm();
        return WordFactory.nullPointer();
    }
}
