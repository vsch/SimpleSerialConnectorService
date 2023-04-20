# Simple Serial Port Connector

A plugin for JetBrains IDEs exposing [jscc - Java Simple Serial Connector] serial library as a service to allow multiple
plugins to share the library and provide serial port connectivity without conflicting with each
other.

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

## Add SimpleSerialConnection to your plugin

* Download the plugin zip file from JetBrains Marketplace
* Expand the zip file and copy the `instrumented-SimpleSerialConnectorService-1.0.0.jar` to a
  directory in your plugin for external libraries, for the sake of discussing call it `libs/`
* Modify the gradle build script to add this `jar` as a `compileOnly` dependency:
    
  ```groovy
  dependencies {
    compileOnly(files("libs/SimpleSerialConnectorService-1.0.0.jar"))
  }
  ```

* Add the plugin to `plugin.xml` as a dependency:

  ```xml
    <depends>com.vladsch.plugins.SimpleSerialPortService</depends>
  ```



## Notes

The plugin uses [jscc - Java Simple Serial Connector], Licensed under [GNU Lesser GPL]

[GNU Lesser GPL]: http://www.gnu.org/licenses/lgpl.html
[Issues]: https://github.com/vsch/SimpleSerialConnectorService/issues
[Plugin Source]: https://github.com/vsch/SimpleSerialConnectorService
[jscc - Java Simple Serial Connector]: https://github.com/java-native/jssc

