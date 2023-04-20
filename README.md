# Simple Serial Port Connector

Exposes [jscc] serial library as a service to allow multiple plugins to share the library and
provide serial port connectivity without conflicting with each other.

To make the service compatible with now JetBrains maintained `Serial Monitor` plugin, the
service and associated interfaces were copied into this plugin. If `Serial Monitor` plugin is
installed then this plugin's `SerialPortManager` will delegate all calls to the `Serial Monitor`
plugin's `JsscSerialService`. If the `Serial Monitor` plugin is not installed, then this plugin
will provide the service via the [jscc] library in this plugin.

Effectively, any plugin can implement serial monitoring by including this plugin as a dependency
and seamlessly provide serial port communication with or without `Serial Monitor` being
installed or compatible with an earlier version of the IDE.

<hr>  

This plugin is intended to be installed as a dependency of other plugins since it exposes no
user functionality of its own.

<hr>

| [Issues][] | [Plugin Source][] |



## Notes

The plugin uses [jscc], Licensed under [GNU Lesser GPL]

[GNU Lesser GPL]: http://www.gnu.org/licenses/lgpl.html
[Issues]: https://github.com/vsch/SimpleSerialPortService/issues
[Plugin Source]: https://github.com/vsch/SimpleSerialPortService
[jscc]: https://github.com/java-native/jssc

