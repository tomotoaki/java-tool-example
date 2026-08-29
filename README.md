# java-tool-example

Spring Boot 4.0.6 + MyBatis + H2 で作成した、アクセストークンを管理するシンプルな CLI アプリケーションです。

## 構成

| 役割 | クラス |
|---|---|
| エントリポイント（引数の解釈・出力のみ） | `AccessTokenCliApplication` (CommandLineRunner) |
| 業務ロジック | `AccessTokenService` / `AccessTokenServiceImpl` |
| DB アクセス（MyBatis） | `AccessTokenMapper`（インタフェース）+ `mapper/AccessTokenMapper.xml`（SQL） |
| エンティティ | `AccessToken`（Lombok `@Data` / `@Builder`） |

`AccessTokenCliApplication` は業務ロジックを持たず、`AccessTokenService` インタフェースを呼び出すだけになっています。

## 前提

- Java 17 以上（動作確認は Java 25）
- Maven 3.9 以上

> このリポジトリでは Maven Wrapper を利用するため、ローカルに Maven を別途入れなくても動作します。

## ビルド

```bash
./mvnw clean package
```

`target/java-tool-example.zip` に、jar、README、Windows 用スクリプト、Linux/Mac 用スクリプトが生成されます。

## 実行

データは `./data/tokendb.mv.db`（H2 ファイル DB）に永続化されるため、実行のたびにリセットされません。
DB を初期化したい場合は `data` ディレクトリを削除してください。

### 直接実行 (jar)

```bash
# トークン発行（所有者名、任意で有効日数）
java -jar target/java-tool-example.jar create alice 30
java -jar target/java-tool-example.jar create bob        # 無期限

# 一覧表示
java -jar target/java-tool-example.jar list

# 1件取得
java -jar target/java-tool-example.jar get 1

# 更新（所有者名・有効日数を再設定）
java -jar target/java-tool-example.jar update 1 alice2 60

# 無効化
java -jar target/java-tool-example.jar revoke 1

# 削除
java -jar target/java-tool-example.jar delete 1
```

### Windows (run.bat)

```cmd
run.bat create alice 30
run.bat list
run.bat get 1
run.bat update 1 alice2 60
run.bat revoke 1
run.bat delete 1
```

### Linux/Mac (run.sh)

```bash
chmod +x run.sh
./run.sh create alice 30
./run.sh list
./run.sh get 1
./run.sh update 1 alice2 60
./run.sh revoke 1
./run.sh delete 1
```

## メモ

- MyBatis の `mybatis-spring-boot-starter` は `4.0.1` を使用しています。これは Spring Boot 4.0.x 系に対応したバージョンです。
