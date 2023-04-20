This plugin is intended to be installed as a dependency of other plugins since it exposes no
user functionality of its own. See [README] file for configuring your plugin build to use this
plugin's `JsscSerialService`.

<hr>

Exposes [jscc - Java Simple Serial Connector] serial library as a service to allow multiple
plugins to share the library and provide serial port connectivity without conflicting with each
other.

The service provided by this plugin is a clone of one in the JetBrains `Serial Port Monitor`
plugin. The intention is to use the `Serial Port Monitor` plugin, if it is installed, by delegating
all calls to its `JsscSerialService`. If the `Serial Port Monitor` plugin is not installed, then this
plugin provides the service via the [jscc - Java Simple Serial Connector] library.

Effectively, any plugin can implement serial monitoring by including this plugin as a dependency
and seamlessly provide serial port communication with or without `Serial Port Monitor` being
installed or compatible with an earlier version of the IDE.

Currently, it is not possible to use `Serial Port Monitor`'s classes in a dependent plugin.
Using this library may still conflict with `Serial Port Monitor` loading of native libraries.

Implement serial port connectivity by using this plugin as a dependency, to avoid conflicting
with other plugins is still preferable.

<hr>  

| [Issues][] | [Plugin Source][] |

[Issues]: https://github.com/vsch/SimpleSerialConnectorService/issues
[README]: https://github.com/vsch/SimpleSerialConnectorService/blob/master/README.md
[Plugin Source]: https://github.com/vsch/SimpleSerialConnectorService
[jscc - Java Simple Serial Connector]: https://github.com/java-native/jssc

