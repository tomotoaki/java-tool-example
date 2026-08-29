# Release Workflow

このディレクトリの `release.yml` は、タグを起点にアプリケーションをビルドし、配布用 zip を GitLab Release の Assets に添付します。

## 実行トリガー

### タグの push

`v` プレフィックスで始まるタグを push すると実行されます。

```bash
git tag v1.0.0
git push origin v1.0.0
```

通常のブランチ push や Merge Request では release ジョブは実行されません。

### 手動実行

GitLab の **CI/CD > Pipelines** から **Run pipeline** を実行することで、同じジョブを手動で起動できます。

手動実行ではビルドと zip の存在確認を行いますが、タグ起点ではないため GitLab Release は作成しません。

## 処理内容

1. ソースコードを checkout
2. Maven Wrapper で `./mvnw clean package` を実行
3. `target/java-tool-example.zip` の生成を確認
4. `build` ジョブの Artifact と dotenv 変数を `release` ジョブへ受け渡し
5. タグ起点の場合、GitLab Release を作成して zip を Assets に添付

GitLab の Job artifacts は保存されますが、配布ファイルは Release の Assets にのみ保存されます。

> GitHub の「Temurin Java 25 をセットアップ」に相当する処理は、GitLab ではジョブの `image: maven:3.9-eclipse-temurin-25` で実現しています。

## ルートの CI 定義

ルートの `.gitlab-ci.yml` は次のようにして実体を読み込みます。

```yaml
include:
  - local: '.gitlab/release.yml'
```

この構成により、CI の定義本体は `.gitlab/release.yml` に集約されます。

GitLab の設定は、ルートの `.gitlab-ci.yml` から分離した実体を読み込む設計です。

## リリース運用

初回リリースは `main` ブランチ上でタグを作成します。

```bash
git switch main
git pull origin main
git tag v1.0.0
git push origin v1.0.0
```

リリース後に保守が必要になった場合は、リリースタグから `1.0.x` ブランチを作成します。

```bash
git switch -c 1.0.x v1.0.0
git push -u origin 1.0.x
```

`1.0.x` ブランチで修正を行った場合は、`v1.0.1`、`v1.0.2` のようにタグを作成してリリースします。

```bash
git switch 1.0.x
# 修正して commit

git tag v1.0.1
git push origin v1.0.1
```

## ローカルでの確認

ローカルでは次のコマンドで同じ配布 zip を生成できます。

```bash
./mvnw --batch-mode --no-transfer-progress clean package
```

生成物は `target/java-tool-example.zip` です。
