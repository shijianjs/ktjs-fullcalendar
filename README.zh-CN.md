# ktjs-fullcalendar

FullCalendar 7 的 Kotlin/JS 绑定 —— 日历、视图、事件与插件

封装覆盖 `fullcalendar` 7 的 vanilla 日历：`createCalendar`、带类型的 `CalendarOptions`、事件与事件源、视图切换与翻页，以及全部开源视图插件（day grid、time grid、multi-month、list、interaction、all）。子路径与插件值都通过 external object 绑定 —— 否则 Kotlin/JS 会为「只有 default 导出」的子路径生成默认导入。

- 上游包：[`fullcalendar`](https://github.com/fullcalendar/fullcalendar) 7.1.5
- 文档：https://shijianjs.github.io/ktjs-fullcalendar/
- 演示：https://shijianjs.github.io/ktjs-fullcalendar/demo/
- API：https://shijianjs.github.io/ktjs-fullcalendar/api/
- 英文说明：[README.md](README.md)

## 用法

```kotlin
implementation("io.github.shijianjs:ktjs-fullcalendar:7.1.5-kt.1")
```

## 构建

```bash
./gradlew build
./gradlew :ktjs-fullcalendar:jsBrowserTest
./gradlew :demo:e2eTest
./gradlew pagesSite
```

## 许可证

wrapper 与上游 `fullcalendar` 同为 MIT；版权声明见文档的「许可证」页。
