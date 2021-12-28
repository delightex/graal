dstDir=src/main/java/com/oracle/svm/core/annotate
srcDir=../substratevm/src/com.oracle.svm.core/src/com/oracle/svm/core/annotate


mkdir -p $dstDir

cp $srcDir/Substitute.java $dstDir/
cp $srcDir/TargetClass.java $dstDir/
