(function (angular) {
    "use strict";

    angular
        .module("app.user")
        .controller("UserAddController", UserAddController);

    UserAddController.$inject = ["userService", "categoryService", "$state", "$window"]
    function UserAddController(userService, categoryService, $state, $window) {
        var viewModel = this;

        viewModel.newUser = {};
        viewModel.newUser.firstName = "";
        viewModel.newUser.lastName = "";
        viewModel.newUser.username = "";
        viewModel.newUser.password = "";
        viewModel.newUser.reTypedPassword = "";
        viewModel.newUser.type = null;
        viewModel.newUser.categoryId = null;
        viewModel.usedUsername = false;
        viewModel.categoryList = [];
        viewModel.addUser = addUser;

        categoryService
            .getAllCategories()
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.categoryList = response.data;
                }
            });

        function addUser(isValid) {
            if (isValid) {
                var firstName = viewModel.newUser.firstName;
                var lastName = viewModel.newUser.lastName;
                var username = viewModel.newUser.username;
                var password = viewModel.newUser.password;
                var type = viewModel.newUser.type;
                var categoryId = viewModel.newUser.categoryId;

                if (type == 'ROLE_ADMIN') {
                    categoryId = null;
                }

                userService
                    .addUser(firstName, lastName, username, password, type, categoryId)
                    .then(function (response) {
                        $window.alert("User is added.");

                        if (response.status == 200) {
                            $state.go("user-profile", {userId: response.data.id});
                        }
                        else {
                            $state.go("user-list");
                        }
                    }, function (response) {
                        $window.alert("User is not added.");

                        if (response.status == 400) {
                            viewModel.usedUsername = true;
                        }
                    });
            }
        }
    }
} (angular));
