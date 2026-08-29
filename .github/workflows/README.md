# Release Workflow

このディレクトリの `release.yml` は、タグを起点にアプリケーションをビルドし、配布用 zip を GitHub Release の Assets に添付します。

## 実行トリガー

### タグの push

`v` プレフィックスで始まるタグを push すると実行されます。

```bash
git tag v1.0.0
git push origin v1.0.0
```

通常のブランチ push や Pull Request では実行されません。

### 手動実行

GitHub の **Actions** から **Build and Release** を選択し、**Run workflow** を実行できます。

手動実行ではビルドと zip の存在確認を行いますが、タグ起点ではないため GitHub Release は作成しません。

## 処理内容

1. ソースコードを checkout
2. Temurin Java 25 をセットアップ
3. Maven の依存関係をキャッシュ
4. `./mvnw --batch-mode --no-transfer-progress clean package` を実行
5. `target/java-tool-example.zip` の生成を確認
6. タグ起点の場合、GitHub Release を作成して zip を Assets に添付

Actions artifact は保存しません。配布ファイルは GitHub Release の Assets のみに保存されます。

## 権限

Release 作成と Assets のアップロードに必要なため、workflow に次の権限を設定しています。

```yaml
permissions:
  contents: write
```

認証には GitHub Actions が自動提供する `github.token` を使用します。追加の secret や外部 Action は Release 作成のためには必要ありません。

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

workflow と同じ配布 zip は、ローカルでも次のコマンドで生成できます。

```bash
./mvnw --batch-mode --no-transfer-progress clean package
```

生成物は `target/java-tool-example.zip` です。
