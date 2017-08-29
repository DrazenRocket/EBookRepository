(function (angular) {
    "use strict";

    angular
        .module("app.category")
        .controller("CategoryEditController", CategoryEditController);

    CategoryEditController.$inject = ["categoryService", "$state", "$stateParams", "$window"];
    function CategoryEditController(categoryService, $state, $stateParams, $window) {
        var viewModel = this;

        viewModel.categoryId = $stateParams.categoryId;
        viewModel.editedCategory = {};
        viewModel.editedCategory.name = "";
        viewModel.editCategory = editCategory;

        categoryService
            .getCategoryById(viewModel.categoryId)
            .then(function (response) {
                if (response.status == 200) {
                    var category = response.data;

                    viewModel.editedCategory.name = category.name;
                }
            });

        function editCategory(isValid) {
            if (isValid) {
                categoryService
                    .editCategory(viewModel.categoryId, viewModel.editedCategory.name)
                    .then(function (response) {
                        if (response.status == 200 || response.status == 204) {
                            $window.alert("Category is edited.");
                            $state.go("category-detail", {categoryId: viewModel.categoryId});
                        }
                    }, function (response) {
                        if (response.status == 404) {
                            $window.alert("Category is not edited because it does not exists.");
                            $state.go("category-list");
                        }
                        else {
                            $window.alert("Category is not edited.");
                        }
                    });
            }
        }
    }
} (angular));
