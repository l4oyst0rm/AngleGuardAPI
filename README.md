# AngleGuard AntiCheat API

Welcome to the **AngleGuardAPI** documentation! Below, you’ll find instructions for integrating the AngleGuardAPI into your project using **Maven** or **Gradle**. The API is designed to work seamlessly with the AngleGuard AntiCheat system, ensuring compatibility and ease of use.

## Including AngleGuardAPI in Your Project

### Using Maven
To include the AngleGuardAPI in your Maven project, add the following repository and dependency to your `pom.xml` file:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.l4oyst0rm</groupId>
        <artifactId>AngleGuardAPI</artifactId>
        <version>1.0.0-BETA</version>
    </dependency>
</dependencies>
```

### Using Gradle
To include the AngleGuardAPI in your Gradle project, add the following repository and dependency to your `build.gradle` file:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.l4oyst0rm:AngleGuardAPI:1.0.0-BETA'
}
```
