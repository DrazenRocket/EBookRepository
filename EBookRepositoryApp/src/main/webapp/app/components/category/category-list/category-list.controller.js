(function (angular) {
    "use strict";

    angular
        .module("app.category")
        .controller("CategoryListController", CategoryListController);

    CategoryListController.$inject = ["categoryService", "userService", "$state"];
    function CategoryListController(categoryService, userService, $state) {
        var viewModel = this;

        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.loggedInUserId = -1;
        viewModel.loggedInUserDetails = {};
        viewModel.categoryList = [];
        viewModel.showCategoryDetails = showCategoryDetails;

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

        categoryService
            .getAllCategories()
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.categoryList = response.data;
                }
            });

        function showCategoryDetails(categoryId) {

        }
    }
} (angular));
