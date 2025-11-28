# Spring Security でログインできなかった原因メモ

## 現象

- `/login` 画面で正しいユーザー名・パスワードを入力しても  
  `/study-logs` などに遷移せず、**再度 `/login` に戻ってくる**。
- わざと間違ったログイン情報を入れても  
  `/login?error` にリダイレクトされず、**エラーメッセージも表示されない**。

## 原因

- ログインフォームの `<form>` タグが次のようになっていた。

  ```html
  <form method="post" action="/login">
  ```

- Spring Security ではデフォルトで CSRF 対策が有効になっている。
- 上記のように **静的な `action` 属性だけ** を使っていると、
  フォームに CSRF トークンが自動付与されない。
- その結果、`/login` への POST リクエストが  
  **CSRF チェックで拒否され、認証処理まで到達していなかった**。
  （ブラウザ上は 302 で `/login` に戻るだけに見える）

## 対処内容

- [login.html](cci:7://file:///c:/Users/takai/Projects/02_SCT/study-marathon/src/main/resources/templates/auth/login.html:0:0-0:0) の `<form>` を Thymeleaf の `th:action` に変更した。

  **修正前**

  ```html
  <form method="post" action="/login">
  ```

  **修正後**

  ```html
  <form th:action="@{/login}" method="post">
  ```

- `th:action` を使うことで、Thymeleaf + Spring Security により
  hidden フィールドで **CSRF トークンが自動的に埋め込まれる**。
- これにより、ログインの POST が正常に CSRF チェックを通過し、
  Spring Security の認証処理が実行されるようになった。

## 結果

- 正しいログイン情報：
  - `/login` → 認証成功 → `/study-logs` へリダイレクト（`defaultSuccessUrl` の設定通り）。
- 間違ったログイン情報：
  - `/login?error` へリダイレクトされ、
  - [login.html](cci:7://file:///c:/Users/takai/Projects/02_SCT/study-marathon/src/main/resources/templates/auth/login.html:0:0-0:0) の

    ```html
    <p th:if="${param.error}"> ... </p>
    ```

    が有効になり、「ユーザー名またはパスワードが正しくありません。」が表示される。

