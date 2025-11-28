# Phase 0 実装計画、開発環境・プロジェクト初期セットアップ


## 設定ファイルの初期設定
- [x] 1-1. `src/main/resources/application.yml`（または `application.properties`）を作成/整理
- [x] 1-2. ポート番号、H2、Thymeleaf などの基本設定を記載
- [x] 1-3. ログレベル（開発用）の設定を追加するか検討

## H2 Console 有効化（必要に応じて）
- [x] 2-1. H2 Console を有効化する設定を追加
- [x] 2-2. セキュリティ設定で H2 Console へのアクセス方法を検討（開発環境のみ許可など）
- [ ] 2-3. 起動後に `http://localhost:8080/h2-console` へアクセスし、画面表示を確認

## トップページ(仮)のルーティングと簡易テンプレート
- [x] 3-1. 仮トップページ用の Controller クラスを作成（例: `HomeController`）
- [x] 3-2. ルーティング `/` を定義し、仮の View(Thymeleaf) を返すように実装
- [x] 3-3. `templates` 配下に `index.html` などの仮テンプレートを作成し、「Study Marathon MVP (仮)」などの文言を表示
- [x] 3-4. Spring Security 設定の都合上、`/` へのアクセス時の挙動（ログイン要/不要）を一度整理し、今回はどう扱うかメモを残す

## 動作確認・記録
- [ ] 4-1. 上記変更を反映後、再度 `mvnw spring-boot:run` で起動し、ブラウザから `/` および `/login` へアクセスして画面表示を確認
- [ ] 4-2. 確認できた画面キャプチャを1〜2枚取得し、後続Phaseのレビューで参照できるように保存（保存場所は別途決定）
