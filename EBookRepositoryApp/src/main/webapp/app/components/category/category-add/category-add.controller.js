(function (angular) {
    "use strict";

    angular
        .module("app.category")
        .controller("CategoryAddController", CategoryAddController);

    CategoryAddController.$inject = ["categoryService", "$state", "$window"];
    function CategoryAddController(categoryService, $state, $window) {
        var viewModel = this;

        viewModel.newCategory = {};
        viewModel.newCategory.name = "";
        viewModel.addCategory = addCategory;

        function addCategory(isValid) {
            if (isValid) {
                categoryService
                    .addCategory(viewModel.newCategory.name)
                    .then(function (response) {
                        if (response.status == 200) {
                            var data = response.data;
                            var categoryId = data.id;

                            $window.alert("Category is added.");
                            $state.go("category-list");   // TODO redirect
                        }
                    }, function (response) {
                        $window.alert("Category is not added successfully.");
                    });
            }
        }
    }
} (angular));
