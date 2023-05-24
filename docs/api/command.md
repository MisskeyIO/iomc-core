# Command API

iomcCoreにコマンドを登録するためのAPIです。

## コマンドの宣言

iomcCoreに限らず、Spigotプラグインにおいてコマンドを登録する場合は、コードだけでなくplugin.ymlへの宣言も必要です。

本プロジェクトでは[build.gradle.kts](/build.gradle.kts)に直接コマンドを宣言できるようにしています。

build.gradle.ktsを開き、 `commands` ブロック内に次のようなコードを追記します。

```kotlin
register("コマンドの名前") {
    description = "コマンドの説明"
    usage = "コマンドの使い方"
    permission = "コマンドに必要な権限文字列"
}
```

* `"コマンドの名前"` は必ず全て小文字でなければなりません。というかマイクラのコマンドは大文字小文字を区別しません。
* `"コマンドの説明"` は `/help` で表示されるので、わかりやすく書くと良いでしょう。
* `"コマンドの使い方"` は、`/fire <プレイヤー名>` などのようにわかりやすく書きましょう。
* `"コマンドに必要な権限文字列"` は命名規則があるので、次のとおりに設定してください。
    * `"iomccore.command.コマンドの名前"`

### 権限の宣言

権限もまた宣言する必要があるため、併せて宣言します。

`permissions` ブロック内に次のようなコードを記述します。

```kotlin
register("iomccore.command.shibaki") {
    default = Permission.Default.OP
}
```

* defaultに代入する値は、コマンドの使用権限によって次のようになります。
    * **全員許可** → `Default.TRUE`
    * **全員禁止** → `Default.FALSE`
    * **OPのみ許可** → `Default.OP`
    * **OPのみ禁止** → `Default.NOT_OP`

## コマンドの実装

WIP
