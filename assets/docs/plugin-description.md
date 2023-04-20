Exposes [jscc] serial library as a service to allow multiple plugins to share the library and
provide serial port connectivity without conflicting with each other.

To make the service compatible with now JetBrains maintained `Serial Monitor` plugin, the
service and associated interfaces were copied into this plugin. The goal was to use `Serial
Monitor` plugin, if it is installed, by delegating all calls to its `JsscSerialService`. If the
`Serial Monitor` plugin is not installed, then this plugin provides the service via the [jscc]
library.

Effectively, any plugin can implement serial monitoring by including this plugin as a dependency
and seamlessly provide serial port communication with or without `Serial Monitor` being
installed or compatible with an earlier version of the IDE.

<hr>  

This plugin is intended to be installed as a dependency of other plugins since it exposes no
user functionality of its own.

<hr>

| [Issues][] | [Plugin Source][] |

[Issues]: https://github.com/vsch/SimpleSerialConnectorService/issues
[Plugin Source]: https://github.com/vsch/SimpleSerialConnectorService
[jscc]: https://github.com/java-native/jssc

