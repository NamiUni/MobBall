### 概要
MOBを捕まえられるボールを実装するプラグインです。

### 仕様
・捕まえられるMOBは友好的なMOBなど一部のMOBに限られています。

・捕まえられないMOBや地面に向かって空っぽのボールを投げても消費されません。

・捕まえたMOBはボールを投げると召喚できます。

・ボールを投げてMOBが召喚されるとボールは消費されて無くなります。

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
