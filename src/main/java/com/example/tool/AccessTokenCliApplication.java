package com.example.tool;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.tool.entity.AccessToken;
import com.example.tool.service.AccessTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * アクセストークン管理 CLI アプリケーションのエントリポイント。
 *
 * 業務ロジックは一切持たず、コマンドライン引数を解釈して {@link AccessTokenService} を呼び出し、
 * その結果を標準出力に整形して表示するだけの役割に徹する。
 *
 * 使い方:
 *   java -jar java-tool-example.jar create <owner> [validDays]
 *   java -jar java-tool-example.jar list
 *   java -jar java-tool-example.jar get <id>
 *   java -jar java-tool-example.jar update <id> [owner] [validDays]
 *   java -jar java-tool-example.jar revoke <id>
 *   java -jar java-tool-example.jar delete <id>
 */
@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class AccessTokenCliApplication implements CommandLineRunner {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final AccessTokenService accessTokenService;

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(AccessTokenCliApplication.class, args)));
    }

    @Override
    public void run(String... args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];
        try {
            switch (command) {
                case "create" -> doCreate(args);
                case "list" -> doList();
                case "get" -> doGet(args);
                case "update" -> doUpdate(args);
                case "revoke" -> doRevoke(args);
                case "delete" -> doDelete(args);
                default -> {
                    System.out.println("不明なコマンドです: " + command);
                    printUsage();
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("エラー: " + e.getMessage());
        }
    }

    private void doCreate(String[] args) {
        if (args.length < 2) {
            System.out.println("使い方: create <owner> [validDays]");
            return;
        }
        String owner = args[1];
        Long validDays = args.length >= 3 ? Long.valueOf(args[2]) : null;

        AccessToken created = accessTokenService.issue(owner, validDays);
        System.out.println("トークンを発行しました。");
        printToken(created);
    }

    private void doList() {
        List<AccessToken> tokens = accessTokenService.findAll();
        if (tokens.isEmpty()) {
            System.out.println("登録されているトークンはありません。");
            return;
        }
        tokens.forEach(this::printToken);
    }

    private void doGet(String[] args) {
        if (args.length < 2) {
            System.out.println("使い方: get <id>");
            return;
        }
        Long id = Long.valueOf(args[1]);
        Optional<AccessToken> token = accessTokenService.findById(id);
        if (token.isEmpty()) {
            System.out.println("id=" + id + " のトークンは見つかりませんでした。");
            return;
        }
        printToken(token.get());
    }

    private void doUpdate(String[] args) {
        if (args.length < 2) {
            System.out.println("使い方: update <id> [owner] [validDays]");
            return;
        }
        Long id = Long.valueOf(args[1]);
        String owner = args.length >= 3 ? args[2] : null;
        Long validDays = args.length >= 4 ? Long.valueOf(args[3]) : null;

        AccessToken updated = accessTokenService.update(id, owner, validDays);
        System.out.println("トークンを更新しました。");
        printToken(updated);
    }

    private void doRevoke(String[] args) {
        if (args.length < 2) {
            System.out.println("使い方: revoke <id>");
            return;
        }
        Long id = Long.valueOf(args[1]);
        AccessToken revoked = accessTokenService.revoke(id);
        System.out.println("トークンを無効化しました。");
        printToken(revoked);
    }

    private void doDelete(String[] args) {
        if (args.length < 2) {
            System.out.println("使い方: delete <id>");
            return;
        }
        Long id = Long.valueOf(args[1]);
        boolean deleted = accessTokenService.delete(id);
        System.out.println(deleted ? "トークンを削除しました。id=" + id : "id=" + id + " のトークンは見つかりませんでした。");
    }

    private void printToken(AccessToken token) {
        System.out.printf(
                "id=%d, token=%s, owner=%s, status=%s, createdAt=%s, expiresAt=%s%n",
                token.getId(),
                token.getTokenValue(),
                token.getOwnerName(),
                token.getStatus(),
                token.getCreatedAt() != null ? token.getCreatedAt().format(FORMATTER) : "-",
                token.getExpiresAt() != null ? token.getExpiresAt().format(FORMATTER) : "無期限"
        );
    }

    private void printUsage() {
        System.out.println("""
                使い方:
                  create <owner> [validDays]   トークンを発行する
                  list                          全トークンを一覧表示する
                  get <id>                      指定 id のトークンを表示する
                  update <id> [owner] [validDays]  トークンを更新する
                  revoke <id>                   トークンを無効化する
                  delete <id>                   トークンを削除する
                """);
    }
}
