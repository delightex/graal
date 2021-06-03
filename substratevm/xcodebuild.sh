xcodebuild -sdk iphoneos -arch arm64 -project graal-svm-ios.xcodeproj -scheme graal-svm-arm64-ios-debug
xcodebuild -sdk iphoneos -arch arm64 -project graal-svm-ios.xcodeproj -scheme graal-svm-arm64-ios-release

xcodebuild -sdk iphonesimulator -arch x86_64 -project graal-svm-ios.xcodeproj -scheme graal-svm-x86-64-ios-sim-debug
xcodebuild -sdk iphonesimulator -arch x86_64 -project graal-svm-ios.xcodeproj -scheme graal-svm-x86-64-ios-sim-release

ls -la xcode

lipo -info xcode/graal-svm-arm64-ios-d.a
lipo -info xcode/graal-svm-arm64-ios-r.a
lipo -info xcode/graal-svm-x86-64-ios-simulator-d.a
lipo -info xcode/graal-svm-x86-64-ios-simulator-r.a