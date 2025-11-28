# Study Marathon Phase 1 実行計画 (DB接続とEntity定義)

本ドキュメントは、MVP実行計画書の「Phase 1: DB接続とEntity定義」を詳細化し、実施タスクをチェックボックスで管理できるようにしたものです。

---

## 1. Phase概要

- **目的**
  - 要件定義書の「Users」「Study_Logs」2テーブルに対応するEntityとマイグレーション設定を行い、DBとの接続を確認する。

- **スコープ**
  - `Users` / `StudyLogs` Entity 作成
  - Repository インターフェース作成
  - 簡易データ登録・取得の疎通確認

---

## 2. 実行タスク一覧 (チェックリスト)

### 2-1. プロジェクト・環境前提確認

- [ ] `main` ブランチの最新を取得していることを確認する
- [ ] `mvnw spring-boot:run` でアプリが正常起動することを確認する
- [ ] H2 Console (`/h2-console`) にアクセスできることを確認する

### 2-2. DBスキーマ設計の再確認

- [ ] 要件定義書の「Users」テーブル定義を確認する
- [ ] 要件定義書の「Study_Logs」テーブル定義を確認する
- [ ] 今回のPhase1で実装対象とするカラム(必須/任意)を決める
- [ ] 将来拡張(ランキング用など)に関係しそうなカラムがあればメモしておく

### 2-3. Users Entity の実装

- [ ] `Users` Entity クラスを作成する
- [ ] フィールド `id`, `username`, `password` を定義する
- [ ] 主キー(`@Id`, `@GeneratedValue`)の設定を行う
- [ ] `username` の制約(一意制約を付けるかなど)を検討し、必要であれば付与する
- [ ] `password` の長さ/Null許可など、最低限の制約を付ける

### 2-4. StudyLogs Entity の実装

- [ ] `StudyLogs` Entity クラスを作成する
- [ ] フィールド `id`, `user`, `studyDate`, `duration`, `content` を定義する
- [ ] 主キー(`@Id`, `@GeneratedValue`)の設定を行う
- [ ] `studyDate` を日付型(LocalDateなど)で定義する
- [ ] `duration` を分数(int など)で定義し、0以下は不可とする方針を決めておく
- [ ] `content` の文字数上限・Null可否を検討する

### 2-5. Users – StudyLogs のリレーション設定

- [ ] `Users` と `StudyLogs` の関連を JPA アノテーションで定義する
- [ ] `StudyLogs` 側に `@ManyToOne` で `Users` を参照させる
- [ ] 片方向か双方向(`Users` 側に `@OneToMany`)にするかを決める
- [ ] 取得時のフェッチタイプ(LAZY/EAGER)の方針を決める

### 2-6. Repository インターフェースの作成

- [ ] `UsersRepository` (例: `JpaRepository<Users, Long>`) を作成する
- [ ] `StudyLogsRepository` (例: `JpaRepository<StudyLogs, Long>`) を作成する
- [ ] 必要であれば、ユーザー別 or 日付順の検索メソッドを定義する

### 2-7. DBマイグレーション/スキーマ反映

- [ ] `spring.jpa.hibernate.ddl-auto` の設定方針を確認する (`update` か `create-drop` など)
- [ ] アプリ起動時に `Users` / `StudyLogs` テーブルが自動生成されることを確認する
- [ ] H2 Console からテーブル構造を確認し、想定どおりのカラムが作成されているかチェックする

### 2-8. 疎通確認用のデータ投入 (CommandLineRunner など)

- [ ] 起動時にテストデータを投入する `CommandLineRunner` または初期化クラスを作成する
- [ ] `Users` テーブルにテストユーザー(例: `test-user`)を1〜2件登録する
- [ ] `StudyLogs` テーブルに数件のテストログを登録する
- [ ] 起動時にログへ出力して、登録されたデータを確認できるようにする

### 2-9. 簡易取得ロジックとログ出力

- [ ] `UsersRepository` / `StudyLogsRepository` を利用して、全件取得する処理を実装する
- [ ] 取得結果を `logger.info` などでコンソールに出力する
- [ ] 取得件数・代表的なレコード内容をログで確認する

### 2-10. 動作確認・エラー確認

- [ ] `mvnw spring-boot:run` でアプリを起動し、エラーなく起動することを確認する
- [ ] コンソールログに、テストデータ投入・取得ログが出力されていることを確認する
- [ ] H2 Console から `SELECT` を実行し、実データが登録されていることを確認する
- [ ] 想定外のエラーや例外が出ていないかログを確認する

### 2-11. フォローアップ・メモ

- [ ] Phase2 以降で見直すべき点(例えば制約やインデックス)があればメモする
- [ ] 実際に行ったタスクのチェック状態をコミット前に更新する

---

## 3. UI/画面観点での最小確認

Phase1のメインはDB・Entityだが、必要に応じて以下を任意で確認する。

- [ ] シンプルなテスト用Controllerを用意し、`StudyLogsRepository` 経由で取得したデータを画面 or JSON で返す
- [ ] ブラウザ経由でアクセスし、DBから値が取得できていることを確認する

---

## 4. 完了条件

- [ ] `Users` / `StudyLogs` Entity と Repository が実装されている
- [ ] アプリ起動時にテーブルが自動生成される
- [ ] テストデータの投入と取得ができ、ログまたは画面で内容を確認できる
- [ ] 今後のPhaseで利用できる最小限のDB土台が整っている
