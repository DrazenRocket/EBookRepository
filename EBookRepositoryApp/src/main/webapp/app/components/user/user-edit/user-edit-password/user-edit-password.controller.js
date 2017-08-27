(function (angular) {
    "use strict";

    angular
        .module("app.user")
        .controller("UserEditPasswordController", UserEditPasswordController);

    UserEditPasswordController.$inject = ["userService", "$state", "$stateParams", "$window"]
    function UserEditPasswordController(userService, $state, $stateParams, $window) {
        var viewModel = this;

        viewModel.loggedInUserId = userService.getUserIdFromLocalStorage();
        viewModel.loggedUserDetails = {};
        viewModel.userId = $stateParams.userId;
        viewModel.userDetails = {};
        viewModel.oldPassword = "";
        viewModel.newPassword = "";
        viewModel.reTypedNewPassword = "";
        viewModel.unsuccessfulEdit = false;
        viewModel.editPassword = editPassword;

        userService
            .getUserById(viewModel.userId)
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.userDetails = response.data;
                }
            }, function (response) {
                if (response.status == 404) {
                    $window.alert("User is not found. Maybe, user doesn't exist.");
                }
                else {
                    $window.alert("Info about user are not received successfully.");
                }
                // TODO Change state. Redirect to search state
            });

        function editPassword(isValid) {
            if (isValid) {
                userService
                    .changePassword(viewModel.userId, viewModel.oldPassword, viewModel.newPassword)
                    .then(function (response) {
                        if (response.status == 200 || response.status == 204) {
                            $state.go("user-profile", {userId: viewModel.userId});
                        }
                    }, function (response) {
                        if (response.status == 400) {
                            viewModel.unsuccessfulEdit = true;
                        }
                        else if (response.state == 404) {   // User is tried to change password of user which doesn't exist anymore.
                            $window.alert("User doesn't exist.");
                            // TODO Change state. Redirect to search state
                        }
                        else {
                            $window.alert("Changing password is not successfully done because of some error");
                        }
                    });
            }
        }
    }
} (angular));
