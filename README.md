# Misskey for Android v2
MisskeyのためのAndroidクライアント（作りかけ）
## Description
これはMisskeyのためのAndroidクライアントです。これは公式のような名前をしていますが非公式クライアントです。
## Demo
Timeline（Home Timeline)しか動かない
## Usage
TimelineとReply Renote　Reactionと投稿はできる
使えたもんじゃない

# Requirement
直接自分のAPI KEYをソースコードに記述して利用する方法しか確立していない
のでソースコードを加える必要がある
package org.panta.misskey_for_android_v2.constant;
にApplicationConstantというJavaのabstractなクラスを作り
以下のように入力しauthKeyに自分のAPI KEYを入力するくれぐれもGitHubなどに公開しないように！！
gitignoreにApplicationConstant.javaを記述しましょう。
これはどう考えても危険で汎用性を下げているのでアプリの進捗次第で変更する。
```
public abstract class ApplicationConstant {
    final static int timelineGetLimit = 1; //これは機能していない
    public final static String authKey = "XXXXXXXX;
    public final static String domain="https://misskey.xyz";
}
```

採用したアーキテクチャはMVP風の何かであるがMVPではない
ライセンス表記、著作権的に問題のあるファイルがあればすぐに報告してくださいよろしくお願いします。

## install
https://android.gcreate.jp/212 ※私のサイトではない

## Licence
This application is open source software licensed under the GNU AGPLv3.

