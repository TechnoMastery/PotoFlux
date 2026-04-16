package net.minheur.potoflux.login.perms;

import net.minheur.potoflux.Functions;
import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.login.Account;
import net.minheur.potoflux.login.InvalidTokenException;
import net.minheur.potoflux.login.RequestPoster;
import net.minheur.potoflux.login.TokenHandler;
import net.minheur.potoflux.login.response.BaseResponse;
import net.minheur.potoflux.login.response.InfoResponse;
import net.minheur.potoflux.login.response.ListUserResponse;
import net.minheur.potoflux.login.response.RmUserResponse;
import net.minheur.potoflux.translations.Translations;
import net.minheur.potoflux.ui.dialogs.AddUserDialog;
import net.minheur.potoflux.ui.dialogs.AllUsersDialog;
import net.minheur.potoflux.ui.dialogs.RmUserDialog;
import net.minheur.potoflux.utils.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.login.ConnectionHandler.account;
import static net.minheur.potoflux.login.ConnectionHandler.fillPerms;
import static net.minheur.potoflux.ui.UiUtils.*;

public class PermRuns {
    static void addUser() {
        AddUserDialog dialog = new AddUserDialog(PotoFlux.app.getFrame());
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) return;

        String email = dialog.getEmail();
        String password = dialog.getPassword();
        String firstName = dialog.getFirstName();
        String lastName = dialog.getLastName();
        int rank = dialog.getRank();

        List<Perms> perms = dialog.getSelectedPerms();
        List<String> permsCode = new ArrayList<>();

        for (Perms p : perms)
            permsCode.add(p.getCode());

        if (!(rank >= 0 && rank <= 100)) {
            showErrorPane(Translations.get("potoflux:tabs.account.addUser.outOfRangeRank"));
            return;
        }

        String content;
        try {
            content = RequestPoster.addUser(
                    TokenHandler.get(),
                    email, password,
                    firstName, lastName,
                    permsCode.toArray(new String[0]),
                    rank
            );
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
            return;
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.failed"));
            return;
        }

        BaseResponse response = Json.GSON.fromJson(content, BaseResponse.class);
        if (response.success) {
            showMessagePane(Functions.formatMessage(
                    Translations.get("potoflux:tabs.account.addUser.success"),
                    email
            ));
            return;
        }

        showErrorPane(
                switch (response.error) {
                    case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                    case "email_used" -> Translations.get("potoflux:tabs.account.createAccount.emailUsed");
                    case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                    case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                    case "rank_too_big" -> Functions.formatMessage(Translations.get("potoflux:tabs.account.error.rankToBig"),
                            rank, account.rank);
                    default -> response.error;
                }
        );
    }

    public static void rmUser() {
        RmUserDialog dialog = new RmUserDialog(PotoFlux.app.getFrame());
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) return;

        String email = dialog.getEmail();

        String content;
        try {
            content = RequestPoster.rmUser(
                    TokenHandler.get(),
                    email
            );
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
            return;
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.failed"));
            return;
        }

        RmUserResponse response = Json.GSON.fromJson(content, RmUserResponse.class);
        if (response.success) {
            showMessagePane(Functions.formatMessage(
                    Translations.get("potoflux:tabs.account.rmUser.success"),
                    email
            ));
            return;
        }

        showErrorPane(
                switch (response.error) {
                    case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                    case "no_user" -> Functions.formatMessage(
                            Translations.get("potoflux:tabs.account.rmUser.notExists"),
                            email
                    );
                    case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                    case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                    case "rank_to_small" -> Functions.formatMessage(
                            Translations.get("potoflux:tabs.account.error.rankToSmall"),
                            response.targetRank, account.rank
                    );
                    case "target_locked" -> Translations.get("potoflux:tabs.account.error.targetLocked");
                    default -> response.error;
                }
        );

    }

    public static void seeUsersInfos() {
        String listContent;
        try {
            listContent = RequestPoster.listUsers(TokenHandler.get());
        } catch (InvalidTokenException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
            return;
        } catch (IOException e) {
            e.printStackTrace();
            showErrorPane(Translations.get("potoflux:tabs.account.failed"));
            return;
        }

        ListUserResponse listResponse = Json.GSON.fromJson(listContent, ListUserResponse.class);
        if (!listResponse.success) {
            showErrorPane(
                    switch (listResponse.error) {
                        case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                        case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                        case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                        default -> listResponse.error;
                    }
            );
            return;
        }

        List<String> usersUuids = List.of(listResponse.list);

        int userFailed = 0;
        String userInfoContent;
        InfoResponse userResponse;
        Account userAccount;
        List<Account> usersAccounts = new ArrayList<>();

        for (String userUuid : usersUuids) {

            if (userUuid.equals(account.uuid)) continue; // don't list own account

            try {
                userInfoContent = RequestPoster.getUserInfos(
                        TokenHandler.get(),
                        userUuid
                );
            } catch (InvalidTokenException e) {
                e.printStackTrace();
                showErrorPane(Translations.get("potoflux:tabs.account.error.tokenMalformed"));
                return;
            } catch (IOException e) {
                e.printStackTrace();
                PtfLogger.error("Failed to reach server for uuid: " + userUuid, LogCategories.CONNEXION_POST);
                userFailed++;
                continue;
            }

            userResponse = Json.GSON.fromJson(userInfoContent, InfoResponse.class);

            if (!userResponse.success) {

                switch (userResponse.error) {
                    case "not_exists", "token_expired", "no_permission" -> {
                        showErrorPane(
                                switch (userResponse.error) {
                                    case "not_exists" -> Translations.get("potoflux:tabs.account.error.token.notExists");
                                    case "token_expired" -> Translations.get("potoflux:tabs.account.error.token.expired");
                                    case "no_permission" -> Translations.get("potoflux:tabs.account.error.noPerm");
                                    default -> listResponse.error;
                                }
                        );
                        return;
                    }
                    default -> {
                        userFailed++;
                        continue;
                    }
                }

            }

            userAccount = new Account();
            userAccount.uuid = userUuid;
            userAccount.email = userResponse.email;
            userAccount.perms = fillPerms(userResponse.perms);
            userAccount.firstName = userResponse.firstName;
            userAccount.lastName = userResponse.lastName;
            userAccount.rank = userResponse.rank;
            userAccount.locked = userResponse.locked;

            usersAccounts.add(userAccount);

        }

        if (userFailed > 0) {
            showErrorPane(Functions.formatMessage(
                    Translations.get("potoflux:tabs.account.listUsers.failedUsers"),
                    userFailed
            ));
        }

        if (usersAccounts.isEmpty()) {
            showMessagePane(
                    Translations.get("potoflux:tabs.account.listUsers.empty")
            );
            return;
        }

        AllUsersDialog dialog = new AllUsersDialog(
                PotoFlux.app.getFrame(),
                usersAccounts
        );

        dialog.setVisible(true);

    }
}
