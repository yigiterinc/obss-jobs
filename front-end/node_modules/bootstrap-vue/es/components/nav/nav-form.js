"use strict";

exports.__esModule = true;
exports.default = exports.BNavForm = exports.props = void 0;

var _vue = _interopRequireDefault(require("../../utils/vue"));

var _vueFunctionalDataMerge = require("vue-functional-data-merge");

var _object = require("../../utils/object");

var _form = require("../form/form");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; var ownKeys = Object.keys(source); if (typeof Object.getOwnPropertySymbols === 'function') { ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function (sym) { return Object.getOwnPropertyDescriptor(source, sym).enumerable; })); } ownKeys.forEach(function (key) { _defineProperty(target, key, source[key]); }); } return target; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

var props = (0, _object.omit)(_form.props, ['inline']); // @vue/component

exports.props = props;

var BNavForm =
/*#__PURE__*/
_vue.default.extend({
  name: 'BNavForm',
  functional: true,
  props: props,
  render: function render(h, _ref) {
    var props = _ref.props,
        data = _ref.data,
        children = _ref.children;
    return h(_form.BForm, (0, _vueFunctionalDataMerge.mergeData)(data, {
      props: _objectSpread({}, props, {
        inline: true
      })
    }), children);
  }
});

exports.BNavForm = BNavForm;
var _default = BNavForm;
exports.default = _default;