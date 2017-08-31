(function (angular) {
    "use strict";

    angular
        .module("app.category")
        .controller("CategoryDetailController", CategoryDetailController);

    CategoryDetailController.$inject = ["categoryService", "userService", "eBookService", "$state", "$stateParams"];
    function CategoryDetailController(categoryService, userService, eBookService, $state, $stateParams) {
        var viewModel = this;

        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.loggedInUserId = -1;
        viewModel.loggedInUserDetails = {};
        viewModel.categoryId = $stateParams.categoryId;
        viewModel.categoryDetails = {};
        viewModel.eBookList = [];
        viewModel.availableContent = false;
        viewModel.showEBookDetails = showEBookDetails;

        if (viewModel.loggedIn) {
            viewModel.loggedInUserId = userService.getUserIdFromLocalStorage();
            userService
                .getUserById(viewModel.loggedInUserId)
                .then(function (response) {
                    if (response.status == 200) {
                        viewModel.loggedInUserDetails = response.data;
                        viewModel.availableContent = (viewModel.loggedInUserDetails.type == 'ROLE_ADMIN') ||
                            (viewModel.loggedInUserDetails.type == 'ROLE_SUBSCRIBER' &&
                            (viewModel.loggedInUserDetails.category == null ||
                            (viewModel.loggedInUserDetails.category.id == viewModel.categoryId)));
                    }
                });
        }

        categoryService
            .getCategoryById(viewModel.categoryId)
            .then(function (response) {
                if (response.status == 200) {
                    viewModel.categoryDetails = response.data;
                }
            });

        eBookService
            .getAllEBooksByCategoryId(viewModel.categoryId)
            .then(function (response) {
                if (response.status = 200) {
                    viewModel.eBookList = response.data;
                }
            });

        function showEBookDetails(eBookId) {
            $state.go("e-book-detail", {eBookId: eBookId});
        }
    }
} (angular));
