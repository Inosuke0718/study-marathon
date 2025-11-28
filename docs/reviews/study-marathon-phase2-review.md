# Study Marathon Phase2 実装レビュー

対象: `docs/plans/study-marathon-phase2-execution-plan.md`
フェーズ: Phase2 学習記録 CRUD (ログイン前提なし・自分用の簡易CRUD)
ブランチ: `issue/phase2-crud`

---

## 1. 目的・スコープに対する評価

- **目的**
  - 「学習時間の記録」「自分の記録一覧」の基本的な CRUD をログインなしで先に完成させ、画面の流れとバリデーションを固める。
- **総評**
  - `/study-logs` に一覧 + 登録フォーム + 削除ボタンをまとめた画面を用意し、固定ユーザー(`test@example.com`)を使った仮実装ができている。
  - DTO + Service + Repository + Thymeleaf によるレイヤー分離ができており、後続フェーズ(ログイン導入・ユーザー紐付け)への拡張もしやすい構成。
  - Phase2の目的・スコープは十分に満たしていると判断できる。

---

## 2. 実行計画タスクごとのレビュー

### 1. 設計・ルーティング整理 (2-1, 2-2)

- **実装状況**
  - ルーティング:
    - `GET /study-logs` : 学習記録一覧 + 登録フォーム表示
    - `POST /study-logs` : 学習記録の登録
    - `POST /study-logs/{id}/delete` : 学習記録の削除
  - 固定ユーザー方針:
    - `StudyLogService#getFixedUser()` で `test@example.com` のユーザーを取得して利用。
- **評価**
  - Phase2で想定していた「ログインなしで自分用CRUDを触る」前提と整合している。
  - 固定ユーザー取得をService内に閉じ込めており、後続PhaseでSecurityContextから取得する実装に差し替えやすい設計になっている。

### 2. Controller 層の実装 (2-3〜2-5)

- **実装クラス**
  - `com.example.study_marathon.controller.StudyLogController`
- **主なメソッド**
  - `GET /study-logs` (`showLogs`)
    - `StudyLogService#getLatestLogs()` でログ一覧を取得し、Modelに `logs` として渡す。
    - `StudyLogForm` を `@ModelAttribute` で受け取り、`studyDate` が未設定の場合は `LocalDate.now()` で初期化。
  - `POST /study-logs` (`createLog`)
    - `@Valid StudyLogForm` + `BindingResult` によるバリデーション。
    - エラー時は `logs` を再取得して同じテンプレートを返却。
    - 正常時は `redirect:/study-logs` にリダイレクト。
  - `POST /study-logs/{id}/delete` (`deleteLog`)
    - 指定IDのログを削除し、`redirect:/study-logs` にリダイレクト。
- **評価**
  - 一覧表示・登録・削除の責務が明確に分かれており、Controllerの役割として適切。
  - バリデーションエラー時にも一覧を再取得して再表示しており、UIの一貫性も確保されている。

### 3. Service 層の実装 (2-6, 2-7)

- **実装クラス**
  - `com.example.study_marathon.service.StudyLogService`
- **主なメソッド**
  - `getFixedUser()`
    - `UsersRepository#findByEmail("test@example.com")` で仮ユーザーを取得。
  - `getLatestLogs()`
    - `StudyLogsRepository#findTop20ByUserOrderByStudyDateDesc(user)` を利用し、最新20件を取得。
  - `createLog(StudyLogForm form)`
    - DTOから `StudyLogs` エンティティを組み立て、`createdAt` に `LocalDateTime.now()` を設定して保存。
  - `deleteLog(Long id)`
    - `StudyLogsRepository#deleteById(id)` で削除。
- **評価**
  - ControllerがRepositoryを直接触らず、CRUDロジックをServiceに集約できている。
  - 仮ユーザーの扱いもService内に隠蔽されており、責務分担が明確。

### 4. Repository 層の実装 (2-8)

- **実装クラス**
  - `com.example.study_marathon.repository.StudyLogsRepository`
- **主なメソッド**
  - 既存:
    - `findByUserOrderByStudyDateDesc(Users user)`
    - `findByUserAndStudyDateBetweenOrderByStudyDateAsc(Users user, LocalDate start, LocalDate end)`
  - 追加:
    - `findTop20ByUserOrderByStudyDateDesc(Users user)`
- **評価**
  - 「全件 or 上位N件」という要件に対し、上位20件に制限するメソッドを用意しており、一覧画面向けには妥当な設計。
  - 今後、ページングが必要になった場合でも、メソッド追加または`Pageable`対応で拡張可能な構成。

### 5. 画面(Thymeleaf)の作成 (2-9〜2-11)

- **テンプレート**
  - `src/main/resources/templates/study_logs/index.html`
- **構成**
  - 上部: 新規登録フォーム
    - `th:object="${studyLogForm}"` でDTOとバインド。
    - `studyDate` / `duration` / `content` を入力するフィールドを配置。
  - 下部: 記録一覧テーブル
    - `th:each="log : ${logs}"` でログ一覧を表示。
    - 各行に `POST /study-logs/{id}/delete` の削除ボタンを配置。
- **評価**
  - 要件どおり、「フォーム + 一覧 + 削除ボタン」が1画面にまとまっており、ユーザーの作業フローがシンプル。
  - View側のロジックは最小限で、主に表示とフォーム送信に集中している点も良い。

### 6. 入力バリデーション (2-12〜2-14)

- **フォームクラス**
  - `com.example.study_marathon.dto.StudyLogForm`
- **バリデーション内容**
  - `studyDate` : `@NotNull`
  - `duration` : `@NotNull`, `@Min(1)`, `@Max(1440)`
  - `content` : `@NotBlank`
- **画面でのエラー表示**
  - 各フィールドの直下に `th:if="${#fields.hasErrors('fieldName')}"` + `th:errors` でエラーメッセージを表示。
- **評価**
  - 「日付」「分数」「内容」の必須チェックと、分数の範囲チェック(1〜1440分)が入っており、Phase2のレベルとしては十分。
  - 依存として `spring-boot-starter-validation` をpomに追加しており、`jakarta.validation` アノテーションを正しく利用できる構成になっている。

### 7. 動作確認 (2-15〜2-18)

- **想定される確認内容**
  - 2-15 正常系:
    - 正しい値を入力して登録 → 下部一覧に即座に反映されること。
  - 2-16 削除系:
    - 削除ボタン押下 → 対象行が削除されること。
  - 2-17 異常系:
    - 分数を0以下/極端に大きい/未入力、内容空欄など → エラー表示され、登録されないこと。
  - 2-18 二重登録防止:
    - ブラウザのリロードや戻る操作で意図しない二重登録が起きないかの確認。
- **コメント**
  - 実装としては上記シナリオをカバーできる構成になっている。
  - 実際の手動テスト結果に応じて、必要に応じてメッセージ文言やUI調整を行う余地がある。

---

## 3. 良い点のまとめ

- Controller / Service / Repository / DTO / View のレイヤー分離が明確で、責務が整理されている。
- 固定ユーザー取得ロジックを Service に閉じ込めており、後続Phaseでのユーザー紐付け変更がやりやすい。
- バリデーションとエラーメッセージ表示が実装されており、「画面の流れとバリデーションを固める」というPhase2の目的に合致している。
- 一覧取得を最新20件に制限するなど、実運用を意識した設計になっている。

---

## 4. 改善・拡張の候補（任意）

Phase2のスコープを超えない範囲で、今後検討してもよさそうな点:

- **表示ラベルの工夫**
  - 一覧が「最新20件」であることを画面上の文言で明示しておくと、ユーザーに優しい。
- **内容フィールドの長さ制約**
  - Entity側では `content` に `length = 500` が設定されているため、`StudyLogForm` に `@Size(max = 500)` を追加すると、DB制約とフォーム制約が揃う。
- **テストコードの追加**
  - 将来的には、Controller/Service単位のテスト(正常系・異常系)を追加すると、リファクタリング時の安心感が増す。

---

## 5. Phase2としての結論

- 実行計画(Phase2)で定義したタスク 2-1〜2-14 に対応する実装は完了しており、目的・スコープとの整合性も取れている。
- 残タスクは、手動での動作確認(2-15〜2-18)と、その結果に応じた微調整のみ。
- 現時点の実装は、次フェーズ(ユーザー管理・ログイン、ユーザー紐付け)に進むための土台として十分な品質と判断できる。
