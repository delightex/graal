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
        dumpReg(log, "PC", sigcontext.pc(), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "SP", sigcontext.sp(), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
        dumpReg(log, "FP", sigcontext.fp(), printLocationInfo, allowJavaHeapAccess, allowUnsafeOperations);
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
        return WordFactory.pointer(sigcontext.sp());
    }

    @Override
    public PointerBase getIP(ucontext_t uContext) {
        Signal.MContext64Arm sigcontext = uContext.uc_mcontext64_arm();
        return WordFactory.pointer(sigcontext.pc());
    }
}
