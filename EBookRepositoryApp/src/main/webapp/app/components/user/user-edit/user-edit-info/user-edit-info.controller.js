(function (angular) {
    "use strict";

    angular
        .module("app.user")
        .controller("UserEditInfoController", UserEditInfoController);

    UserEditInfoController.$inject = ["userService", "categoryService", "$state", "$stateParams", "$window"];
    function UserEditInfoController(userService, categoryService, $state, $stateParams, $window) {
        var viewModel = this;

        viewModel.userId = $stateParams.userId;
        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.loggedInUserId = -1;
        viewModel.loggedInUserDetails = {};
        viewModel.editedUser = {};
        viewModel.editedUser.firstName = "";
        viewModel.editedUser.lastName = "";
        viewModel.editedUser.username = "";
        viewModel.editedUser.type = null;
        viewModel.editedUser.categoryId = null;
        viewModel.categoryList = [];
        viewModel.usedUsername = false;
        viewModel.editUser = editUser;

        if (viewModel.loggedIn) {
            viewModel.loggedInUserId = userService.getUserIdFromLocalStorage();
            userService
                .getUserById(viewModel.loggedInUserId)
                .then(function (response) {
                    if (response.status == 200) {
                        viewModel.loggedInUserDetails = response.data;
                    }
                })
        }

        userService
            .getUserById(viewModel.userId)
            .then(function (response) {
                if (response.status == 200) {
                    var user = response.data;

                    viewModel.editedUser.firstName = user.firstName;
                    viewModel.editedUser.lastName = user.lastName;
                    viewModel.editedUser.username = user.username;
                    viewModel.editedUser.type = user.type;
                    if (user.category == null) {
                        viewModel.editedUser.categoryId = null;
                    }
                    else {
                        viewModel.editedUser.categoryId = user.category.id;
                    }
                }
            });

        categoryService
            .getAllCategories()
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.categoryList = response.data;
                }
            });

        function editUser(isValid) {
            if (isValid) {
                var userId = viewModel.userId;
                var firstName = viewModel.editedUser.firstName;
                var lastName = viewModel.editedUser.lastName;
                var username = viewModel.editedUser.username;
                var type = viewModel.editedUser.type;
                var categoryId = viewModel.editedUser.categoryId;

                if (type == 'ROLE_ADMIN') {
                    categoryId = null;
                }

                userService
                    .editUser(userId, firstName, lastName, username, type, categoryId)
                    .then(function (response) {
                        if (response.status == 200 || response.status == 204) {
                            $window.alert("User data are updated.");
                            $state.go("user-profile", {userId: userId});
                        }
                    }, function (response) {
                        if (response.status == 400) {
                            viewModel.usedUsername = true;
                        }
                        else if (response.status == 404) {
                            $window.alert("User is not found.");
                        }
                        else {
                            $window.alert("User is not edited.");
                        }
                    });
            }
        }
    }
} (angular));
