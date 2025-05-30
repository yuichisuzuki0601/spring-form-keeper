# 📂 Spring FormKeeper

> **Spring MVC におけるフォームの編集中データを、ほぼ自動で保存・復元できるライブラリ**

**Spring FormKeeper** は、Web アプリケーションのフォーム入力内容を自動で保存・復元できる Spring ライブラリです。  
JavaScript 側の実装はピュアなモジュール形式に対応しており、サーバー側で保存するため、**複数端末間でも編集中のデータを復元可能**です。  
Thymeleaf などのテンプレートエンジンは不要で、React などの SPA でも利用できます。

---

## 🔧 主な用途

- プロフィール編集画面などにおける「編集中データ」の自動保存・復元
- ユーザーがうっかりページを閉じた場合でも、入力内容を保持
- 別端末での編集再開

---

## ✨ 特長

- JavaScript 側の導入が簡単（1 行で observe）
- `name` 属性が付いた要素に限定することで安全かつ汎用的
- **サーバーに保存** できるので、端末間でも復元可
- Spring Boot 非依存（Spring WebMVC のみで動作）
- 複数の保存ストレージを選択可能：`local`, `in-memory`, `h2`, `redis` など

---

## ☕ Java 側の設定例

application.yml

```yaml
logging:
  level:
    jp.co.ysd.spring_form_keeper: INFO

spring:
  form-keeper:
    storage: in-memory # local, in-memory, h2, redis
    ttl: 0 # seconds
```

アノテーションによる有効化

```java
@SpringBootApplication
@EnableFormKeeper
public class SampleAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleAppApplication.class, args);
	}

}
```

---

## 🧩 JavaScript 側の使用例

module として読み込む場合

```html
<script type="module">
  import * as formKeeper from "/form-keeper.min.js";

  const id = 1; // e.g. userId
  const viewName = "profileView";

  formKeeper.observe(id, viewName, {
    beforeRestoreMessage: "編集中の情報が見つかりました。書き戻しますか？",
  });

  document.querySelector("#send").addEventListener("click", () => {
    alert("send!");
    formKeeper.clear(id, viewName);
  });

  window.onunload = formKeeper.unobserve;
</script>
```

UMD として読み込む場合

```html
<script src="/form-keeper.umd.min.js"></script>
<script>
  const id = 1; // e.g. userId
  const viewName = "profileView";

  FormKeeper.observe(id, viewName, {
    beforeRestoreMessage: "編集中の情報が見つかりました。書き戻しますか？",
  });

  document.querySelector("#send").addEventListener("click", () => {
    alert("send!");
    FormKeeper.clear(id, viewName);
  });

  window.onunload = FormKeeper.unobserve;
</script>
```

---

## 🛠️ 導入方法

GitHub Packages などへの公開を検討中です。

```xml
<dependency>
  <groupId>jp.co.ysd</groupId>
  <artifactId>spring-form-keeper</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

---

## 📅 ロードマップ

- [x] `in-memory` 対応
- [ ] `h2` 対応
- [ ] `localStorage` 対応（JavaScript 側のみで完結する軽量モード）
- [ ] `redis` 対応（分散環境向け）
- [ ] README.en.md の用意

---

## 🚀 対応 Java バージョンについて

本ライブラリは Java 17 以上をビルドターゲットとしており、Java 17 以上の環境での利用を推奨しています。  
これにより、多くの現行環境での互換性を確保しています。

---

## ⚠️ 注意点・制限事項

- 対応は `name` 属性がある `<input>`, `<textarea>`, `<select>` 要素のみ
- `id` のみの要素は対応外

---

## 📜 ライセンス

このプロジェクトは [MIT License](https://opensource.org/licenses/MIT) の下で公開されています。
