---
sidebar_position: 3
title: API 概览
---

# API 概览

## 包结构

| 包 | 内容 |
| --- | --- |
| `io.github.shijianjs.ktjs.fullcalendar` | `FullCalendarModule`、六个子路径模块对象、`dayGridPlugin` / `timeGridPlugin` / `multiMonthPlugin` / `listPlugin` / `interactionPlugin` / `allPlugins` 属性与 `createCalendar` |
| `…fullcalendar.core` | `Calendar`、`CalendarOptions`、`ToolbarInput`，以及 `calendarOptions` / `toolbar` / `eventInput` 构造器 |
| `…fullcalendar.core` | `EventInput`、`EventApi`、`EventSourceApi`、`ViewApi`、`EventClickInfo`、`DateClickInfo`、`DateSelectInfo`，以及 `DateInput` / `DurationInput` / `FormatterInput` 联合类型 |

## 本 wrapper 的约定

- 包没有以 ES 具名绑定形式暴露的导出，都通过 external object 取：Kotlin 会把
  `@JsModule("pkg") external object X { @JsName("default") val y }` 编译成 `import * as X from 'pkg'`。
  直接写 `@JsModule` + `@JsName` 声明会编译成默认导入，对 `fullcalendar/*` 在打包阶段就失败。
- `Calendar` 是命名空间里的构造器值，不是 `external` 类声明目标，所以 `createCalendar()` 用一行
  `new` shim 创建实例。
- 联合类型的入参（`DateInput`、`DurationInput`）是不透明接口 + 每个分支一个同名工厂：
  `DateInput("2026-11-16")`、`DateInput(date)`、`DateInput(1_760_000_000_000.0)`。
- 选项与事件都用 `unsafeJso` 构造器：`calendarOptions { … }`、`eventInput { … }`、`toolbar { … }`。
- `formatIso(date, true)` 与上游一致，保留时区偏移 —— 它是 ISO **时刻**而不是纯日历日，
  应按时区时间戳处理，不要当作 `"2026-11-16"`。

## 有意放宽的类型

| 成员 | Kotlin 类型 | 原因 |
| --- | --- | --- |
| `CalendarOptions.height` / `dayMaxEvents` / `buttonText` | `JsAny` | 上游把数字、CSS 长度字符串与「按钮名 → 文案」记录混在同一字段 |
| `CalendarOptions.events` | `JsAny` | 上游接受输入数组、函数、URL 字符串或单个输入 |
| `CalendarOptions.eventDidMount` / `eventAdd` / `eventRemove` / `eventChange` / `eventContent` | `(JsAny) -> Unit` | 每个回调收到的上游信息结构不同，逐个建模留到后续 |
| `ToolbarInput.start` / `center` / `end` | `String?` | 上游是逗号分隔的命令字符串，不是类型化枚举 |
| `Calendar.select` / `changeView` 的区间参数 | `JsAny` | 上游接受区间对象，或「起点 + 终点」两个参数 |

每一条都记在 `docs/development/technical-decisions.md`。
