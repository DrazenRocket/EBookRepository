(function (angular) {
    "use strict";
    
    angular
        .module("app.user")
        .controller("UserListController", UserListController);
    
    UserListController.$inject = ["userService", "$state"];
    function UserListController(userService, $state) {
        var viewModel = this;
        
        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.loggedInUserId = -1;
        viewModel.loggedInUserDetails = {};
        viewModel.userList = [];
        viewModel.showUserProfile = showUserProfile;
        
        if (viewModel.loggedIn) {
            viewModel.loggedInUserId = userService.getUserIdFromLocalStorage();
            userService
                .getUserById(viewModel.loggedInUserId)
                .then(function (response) {
                    if (response.status == 200) {
                        viewModel.loggedInUserDetails = response.data;
                    }
                });
        }

        userService
            .getAllUsers()
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.userList = response.data;
                }
            });

        function showUserProfile(userId) {
            $state.go("user-profile", {userId: userId});
        }
    }
} (angular));
