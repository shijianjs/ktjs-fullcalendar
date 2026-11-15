# API Coverage

## 来源与范围

- 来源：`fullcalendar` 7.1.5（npm），源码克隆 `S:\workspace\github\fullcalendar\fullcalendar`
- 声明文件：`packages/vanilla/src/{Calendar.tsx,options.ts}`、`packages/preact/src/{api,interactions,calendar-utils}`，
  以及发布包里的 `index.d.ts`、`daygrid.d.ts`、`timegrid.d.ts`、`multimonth.d.ts`、`list.d.ts`、
  `interaction.d.ts`、`all.d.ts`
- 核对日期：2026-11-16
- 范围：`fullcalendar` 包对 vanilla 用户可见的 API —— `Calendar` 类与其方法、`CalendarOptions`、
  事件与事件源、视图句柄、点击/选择回调的入参，以及六个开源视图插件。
- 不纳入统计：`@full-ui/headless-calendar`、`@fullcalendar/core-types` 的内部类型、`preact`/`react`/
  `vue`/`angular` 集成包（它们是另外的 npm 包，见「明确不做」）。

## 统计

| 状态 | 数量 |
|---|---:|
| 总计 | 26 |
| 已完成 | 12 |
| 明确不做 | 3 |
| 待完成 | 11 |

已完成组（不逐项列名，实现见源码）：

1. `createCalendar` 与 `Calendar` 的构造（`FullCalendar.kt` 的 `newCalendar` shim）
2. 六个视图插件值与 `dayGridPlugin` / `timeGridPlugin` / `multiMonthPlugin` / `listPlugin` /
   `interactionPlugin` / `allPlugins` 访问器（`FullCalendar.kt`）
3. `CalendarOptions` 的常用字段与 `calendarOptions` 构造器（`core/Options.kt`）
4. `ToolbarInput` 与 `toolbar` 构造器（`core/Options.kt`）
5. `EventInput` 与 `eventInput` 构造器（`core/Events.kt`）
6. `EventApi`（`core/Events.kt`）
7. `EventSourceApi`（`core/Events.kt`）
8. `ViewApi`（`core/Events.kt`）
9. `EventClickInfo` / `DateClickInfo` / `DateSelectInfo`（`core/Events.kt`）
10. `DateInput` / `DurationInput` / `FormatterInput` 联合与同名工厂（`core/Events.kt`）
11. `Calendar` 的导航与视图方法（`prev`、`next`、`today`、`gotoDate`、`changeView`、`zoomTo`、
    `getDate`、`formatIso`、`formatDate`、`formatRange`）（`core/Calendar.kt`）
12. `Calendar` 的事件方法（`addEvent`、`getEventById`、`getEvents`、`removeAllEvents`、事件源相关、
    `refetchEvents`）（`core/Calendar.kt`）

## 明确不做

| 名称 | 排除理由 |
|---|---|
| Scheduler / Resource Timeline / Resource views | FullCalendar 的商业付费包，不属于开源 `fullcalendar` npm 包；本项目只做开源免费部分 |
| `@full-ui/headless-calendar` | 上游的无 UI 内核，vanilla 用户不会直接使用它；声明在 `packages/headless-calendar`，不属于公开 API 面 |
| `preact` / `react` / `vue` / `angular` 集成包 | 各框架自己的 npm 包（`fullcalendar/react` 之外还有独立的 React/Vue/Angular 包），Kotlin/JS 的 target 不是这些框架，封装它们没有使用者 |

## 待完成

### 选项与格式化

- [ ] `CalendarOptions` 其余选项（`eventTimeFormat`、`slotLabelFormat`、`businessHours`、
      `hiddenDays`、`navLinks`、`eventLimit`、`datesSet`、`loading`、各个 `*ClassNames`）
- [ ] 具名时区支持（`timeZone` 及 `fullcalendar/locales-all` 的语言包）
- [ ] `formatRange` 的 `settings` 联合与 `FormatterInput` 的类型化建模

### API 面

- [ ] `CalendarApi.trigger` / `getOptionSources` / `getRootEl`
- [ ] `EventApi.update` 与 `toPlainObject` 的返回类型
- [ ] 循环事件（`recurringDef`、`daysOfWeek`、`rrule` 插件）
- [ ] `EventSourceApi.subscription` 与 `Subscription` 类型
- [ ] `ViewApi.getOptionSources` 与视图内的日期辅助方法
- [ ] `select` 的区间对象与 `unselect` 的回调参数类型

### 交互

- [ ] 交互插件的高级回调（`eventDrop`、`eventResize`、`eventReceive`、`drop`、`draggable`）
- [ ] `fullcalendar/preact` 子路径的 Preact 组件封装

## 验证依据

| 命令 | 结果 |
|---|---|
| `./gradlew :ktjs-fullcalendar:compileKotlinJs` | 通过 |
| `./gradlew :ktjs-fullcalendar:jsBrowserTest` | 4 个用例全部通过（Chromium） |
| `./gradlew :demo:jsBrowserDistribution` | 通过 |
| `./gradlew :demo:e2eTest` | 通过（Playwright，含视图切换与语言切换） |
| `./gradlew :docs:docusaurusBuild` | en 与 zh-Hans 均成功 |
