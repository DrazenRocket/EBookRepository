(function (angular) {
    "use strict";

    angular
        .module("app.user")
        .controller("UserProfileController", UserProfileController);

    UserProfileController.$inject = ["userService", "$state", "$stateParams", "$window"];
    function UserProfileController(userService, $state, $stateParams, $window) {
        var viewModel = this;

        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.loggedInUserId = -1;
        viewModel.loggedInUserDetails = {};
        viewModel.userId = $stateParams.userId;
        viewModel.userDetails = {};
        viewModel.isOfLoggedInUser = false;
        viewModel.updateProfile = updateProfile;
        viewModel.changePassword = changePassword;

        if (viewModel.loggedIn) {
            viewModel.loggedInUserId = userService.getUserIdFromLocalStorage();
            viewModel.isOfLoggedInUser = viewModel.userId == viewModel.loggedInUserId;
            userService
                .getUserById(viewModel.loggedInUserId)
                .then(function (response) {
                    if (response.status == 200) {
                        viewModel.loggedInUserDetails = response.data;
                    }
                });
        }

        userService
            .getUserById(viewModel.userId)
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.userDetails = response.data;
                }
            });

        function updateProfile() {
            $window.alert("Not yet implemented");
        }

        function changePassword() {
            $window.alert("Not yet implemented");
        }
    }
} (angular));
