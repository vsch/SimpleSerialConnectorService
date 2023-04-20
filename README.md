# Simple Serial Port Connector

A plugin for JetBrains IDEs exposing [jscc - Java Simple Serial Connector] serial library as a
service to allow multiple plugins to share the library and provide serial port connectivity
without conflicting with each other.

To make the service compatible with the JetBrains maintained `Serial Monitor` plugin, the
service and associated interfaces were copied into this plugin.

The idea was that if `Serial Port Monitor` plugin is installed then this plugin's
`JsscSerialService` will delegate all calls to the `Serial Monitor`'s `JsscSerialService`. If
the `Serial Port Monitor` plugin is not installed, then this plugin will provide the service via
its own implementation using the [jscc - Java Simple Serial Connector] library.

Effectively, any plugin can implement serial monitoring by including this plugin as a dependency
and seamlessly provide serial port communication with or without `Serial Port Monitor` being
installed or compatible with an earlier version of the IDE.

The fly in the ointment is that it is not possible to use `Serial Port Monitor`'s classes in a
dependent plugin. This means that for now, using this library may still conflict with `Serial
Port Monitor` loading of native libraries. That said, it is still preferable to implement serial
port connectivity by using this plugin as a dependency, to avoid conflicting with other plugins.

## Adding SimpleSerialConnectorService to your plugin

* Modify the gradle build script to add the github repository for the plugin package, and define
  your github user and a personal access token (see [Creating A Personal Access Token]) in the
  environment variables: `GITHUB_ACTOR`, `GITHUB_TOKEN`.

  ```groovy
  repositories {
    maven {
        name = "GitHubPackages"
        url = "https://maven.pkg.github.com/vsch/SimpleSerialConnectorService"
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
  }
  ```
* Modify the gradle build script to add this `jar` as a `compileOnly` dependency:

  ```groovy
  dependencies {
    compileOnly("com.vladsch.plugins:simple-serial-connector-service:1.0.2")
  }
  ```
* Add the plugin to `plugin.xml` as a dependency:

  ```xml
  <depends>com.vladsch.plugins.SimpleSerialConnectorService</depends>
  ```

## Notes

The plugin uses [jscc - Java Simple Serial Connector], Licensed under [GNU Lesser GPL 3]

[Creating A Personal Access Token]: https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token
[jscc - Java Simple Serial Connector]: https://github.com/java-native/jssc
[GNU Lesser GPL 3]: http://www.gnu.org/licenses/lgpl.html
[Issues]: https://github.com/vsch/SimpleSerialConnectorService/issues
[Plugin Source]: https://github.com/vsch/SimpleSerialConnectorService

