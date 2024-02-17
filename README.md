### 概要
MOBを捕まえられるボールを実装するプラグインです。

### 仕様
ボールを投げてMOBにヒットすると捕まえられます。 
しかし、ボールを外してしまうとボールは消滅します。

捕まえたMOBはボールを投げると召喚できます。
一度捕まえたMOBはスニークしながら右クリックするとボールに戻せます。

### コマンド
`/mobball reload` コンフィグをリロードする

`/mobball give <player> <ball>` 指定したプレイヤーにボールを与える

`/mobball list` 捕まえられるMOBの一覧を表示する

### 権限
`mobball.reload`

`mobball.give`

`mobball.list`

コードの大部分は[Carbon](https://github.com/Hexaoxide/Carbon)から引用しています。
プラグインを無償で公開しているDraycia氏に感謝します。
