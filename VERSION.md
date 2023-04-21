# Version History

[TOC]: #

### Table of Contents
- [1.0.4](#104)
- [1.0.2](#102)
- [1.0.0](#100)

## 1.0.4

* Add: `SerialPortOwnerAccess` interface to provide `write()` method to connected port.
* Remove: `JsscSerialServiceDelegate`. Not used and was causing plugin verification seizures.
* Remove: `write()` method on `JsscSerialService`, use instance of
  `SerialPortOwnerAccess.write()` method instead.

## 1.0.2

* Fix: `plugin.xml` plugin name to `SimpleSerialConnectorService`

## 1.0.0

* Initial release
