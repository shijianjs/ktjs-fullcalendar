# TODO

## ktjs-fullcalendar 上游 API 对齐

把上游 `fullcalendar` 7.1.5-kt.1 的类型声明逐个落成 Kotlin/JS 声明，并让 demo、文档、
测试与之一致。

- 总计：26
- 已完成：12
- 明确不做：3
- 待完成：11

详细清单：[coverage.md](docs/development/coverage.md)
技术决策：[technical-decisions.md](docs/development/technical-decisions.md)

### 当前待办

- [ ] `CalendarOptions` 其余选项（`eventTimeFormat`、`slotLabelFormat`、`businessHours`、`hiddenDays`、`navLinks`、`eventLimit`、`datesSet`、`loading`、各个 `*ClassNames`）
- [ ] 具名时区支持（`timeZone` 及 `fullcalendar/locales-all` 的语言包）
- [ ] `formatRange` 的 `settings` 联合与 `FormatterInput` 的类型化建模
- [ ] `CalendarApi.trigger` / `getOptionSources` / `getRootEl`
- [ ] `EventApi.update` 与 `toPlainObject` 的返回类型
- [ ] 循环事件（`recurringDef`、`daysOfWeek`、`rrule` 插件）
- [ ] `EventSourceApi.subscription` 与 `Subscription` 类型
- [ ] `ViewApi.getOptionSources` 与视图内的日期辅助方法
- [ ] `select` 的区间对象与 `unselect` 的回调参数类型
- [ ] 交互插件的高级回调（`eventDrop`、`eventResize`、`eventReceive`、`drop`、`draggable`）
- [ ] `fullcalendar/preact` 子路径的 Preact 组件封装

### 注意事项

- 只封装 MIT 授权的开源 `fullcalendar` 包；Scheduler / Resource Timeline / Premium 商业插件不在范围内。
- 视图插件必须出现在 `plugins` 里，否则 `initialView` 无效 —— 这一点写进了快速开始。
- 有意放宽的类型逐条记在 `technical-decisions.md`，不得悄悄退回 `JsAny`。
