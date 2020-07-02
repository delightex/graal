#### Building

`mx build --dependencies SULONG_TOOLS`

Building requires headers that are not yet shipped with the LLVM_TOOLCHAIN. Thus, another
llvm installation is needed (ensure that `llvm-config` is on the `$PATH`). Also, llvm 10
does not work as it produces bitcode that not compatible with llvm 9 that is used by the
toolchain.

#### Usage Example

    mx fuzz testfuzz
    mx ll-reduce --clang-input='<path:com.oracle.truffle.llvm.tools.fuzzing.native>/src/fuzzmain.c' testfuzz/<<timestamp>>/autogen.ll
    bugpoint -mlimit=0 -keep-main -opt-command=<<path to opt tool>> -compile-custom -compile-command='mx check-interesting' autogen.ll

If bugpoint erroneously passes duplicated parameters to check-interesting, extract the compile command into a shell script.
