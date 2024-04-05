// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "Android-KMP-Template",
    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "Android-KMP-Template",
            targets: ["Android-KMP-Template"]),
    ],
    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "Android-KMP-Template"),
        .testTarget(
            name: "Android-KMP-TemplateTests",
            dependencies: ["Android-KMP-Template"]),
    ]
)
