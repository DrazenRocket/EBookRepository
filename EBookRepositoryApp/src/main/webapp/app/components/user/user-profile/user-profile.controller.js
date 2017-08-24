(function (angular) {
    "use strict";

    angular
        .module("app.user")
        .controller("UserProfileController", UserProfileController);

    UserProfileController.$inject = ["userService", "$state", "$stateParams", "$window"];
    function UserProfileController(userService, $state, $stateParams, $window) {
        var viewModel = this;

        viewModel.loggedInUserId = userService.getUserIdFromLocalStorage();
        viewModel.loggedInUserDetails = {};
        viewModel.userId = $stateParams.userId;
        viewModel.userDetails = {};
        viewModel.isOfLoggedInUser = viewModel.userId == viewModel.loggedInUserId;
        viewModel.updateProfile = updateProfile;
        viewModel.changePassword = changePassword;

        userService
            .getUserById(viewModel.userId)
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.userDetails = response.data;
                }
            });
        userService
            .getUserById(viewModel.loggedInUserId)
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.loggedInUserDetails = response.data;
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
