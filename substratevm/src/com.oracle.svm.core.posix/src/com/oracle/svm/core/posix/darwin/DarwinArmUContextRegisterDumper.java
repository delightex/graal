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
        dumpReg(log, "R0 ", ((Pointer) sigcontext).readLong(sigcontext.r0_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R1 ", ((Pointer) sigcontext).readLong(sigcontext.r1_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R2 ", ((Pointer) sigcontext).readLong(sigcontext.r2_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R3 ", ((Pointer) sigcontext).readLong(sigcontext.r3_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R4 ", ((Pointer) sigcontext).readLong(sigcontext.r4_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R5 ", ((Pointer) sigcontext).readLong(sigcontext.r5_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R6 ", ((Pointer) sigcontext).readLong(sigcontext.r6_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R7 ", ((Pointer) sigcontext).readLong(sigcontext.r7_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R8 ", ((Pointer) sigcontext).readLong(sigcontext.r8_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R9 ", ((Pointer) sigcontext).readLong(sigcontext.r9_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R10 ", ((Pointer) sigcontext).readLong(sigcontext.r10_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R11 ", ((Pointer) sigcontext).readLong(sigcontext.r11_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R12 ", ((Pointer) sigcontext).readLong(sigcontext.r12_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R13 ", ((Pointer) sigcontext).readLong(sigcontext.r13_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R14 ", ((Pointer) sigcontext).readLong(sigcontext.r14_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R15 ", ((Pointer) sigcontext).readLong(sigcontext.r15_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R16 ", ((Pointer) sigcontext).readLong(sigcontext.r16_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R17 ", ((Pointer) sigcontext).readLong(sigcontext.r17_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R18 ", ((Pointer) sigcontext).readLong(sigcontext.r18_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R19 ", ((Pointer) sigcontext).readLong(sigcontext.r19_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R20 ", ((Pointer) sigcontext).readLong(sigcontext.r20_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R21 ", ((Pointer) sigcontext).readLong(sigcontext.r21_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R22 ", ((Pointer) sigcontext).readLong(sigcontext.r22_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R23 ", ((Pointer) sigcontext).readLong(sigcontext.r23_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R24 ", ((Pointer) sigcontext).readLong(sigcontext.r24_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R25 ", ((Pointer) sigcontext).readLong(sigcontext.r25_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R26 ", ((Pointer) sigcontext).readLong(sigcontext.r26_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R27 ", ((Pointer) sigcontext).readLong(sigcontext.r27_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "R28 ", ((Pointer) sigcontext).readLong(sigcontext.r28_offset()), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
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
        return WordFactory.nullPointer();
    }

    @Override
    public PointerBase getIP(ucontext_t uContext) {
        Signal.MContext64Arm sigcontext = uContext.uc_mcontext64_arm();
        return WordFactory.nullPointer();
    }
}
