# なぜこのプロジェクトは保留となったか

## 🎯 当初の目標

- Java 製ライブラリ（Spring Boot）にスクリプトをバンドルし、Web アプリに導入するだけでフォームの編集中・自動保存・多端末復元などの機能が得られる。
- クライアントとサーバーを完全に同期させ、一体型でメンテナンスできるようにする。

## ❌ 実際に直面した問題

- TypeScript や React といった現代のフロントエンド開発と、グローバルスクリプト注入方式は非常に相性が悪い。
- 型定義、動的ロード、コンポーネントライフサイクルとの整合などで不具合や面倒が多発。
- dynamic import を 外部ライブラリから行うときも ts では型定義が必要。
- 今回 jar にバンドルされているスクリプトは外部ライブラリ扱い。
- 使うためには.d.ts ファイルを手動で用意する必要がある。
- そんな手間を掛けさせては普及するはずがない。
- サーバー側の責務とクライアント側の責務が分離できない構成は、導入障壁が高くなり、普及しづらい。
- 結果、スクリプトを npm で提供する以外に手段がなく、jar にバンドルする構想は時代遅れとなった。

## ✅ 代替案・今後の方向性

- クライアントが Next.js などで動作する前提で、**Next.js 側の API 機能を利用してフォームの状態保存/復元を行う構成**を検討。
- フロントとサーバーを分離し、**npm モジュールで提供するクライアントライブラリ**として再構成。
- サーバー連携が必要な場合でも、**軽量な Node.js ベースのエンドポイント**や、別途 API サーバー構成で対応する。

## 💡 学び

- **アーキテクチャの理想と現実の開発環境とのギャップを早めに見極めることの重要性**
- **失敗を共有することも OSS の価値**

## 🍙 結びとして

- 誰か代わりにやって欲しいなぁ
