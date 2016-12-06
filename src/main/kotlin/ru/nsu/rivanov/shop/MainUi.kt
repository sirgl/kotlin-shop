package ru.nsu.rivanov.shop

import com.vaadin.annotations.Theme
import com.vaadin.data.util.BeanItem
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.server.FontAwesome
import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.*
import org.springframework.beans.factory.annotation.Autowired


@Theme("valo")
@SpringUI(path = "ui")
class MainUi : UI() {
    @Autowired
    lateinit var categoryRepo: CategoryRepository

    override fun init(request: VaadinRequest?) {
        val horizontalLayout = HorizontalLayout()
        horizontalLayout.setSizeFull()
        val grid = Grid()
        listCategories(grid)
        grid.setSizeFull()
        val categoryEditor = CategoryEditor(categoryRepo, { listCategories(grid) })
        horizontalLayout.addComponents(grid, categoryEditor)
        horizontalLayout.setExpandRatio(grid, 2.5f)
        horizontalLayout.setExpandRatio(categoryEditor, 1f)
        grid.addItemClickListener {
            val beanItem = it.item as BeanItem<Category>
            categoryEditor.changeCurrentCategory(beanItem.bean)

        }
        content = horizontalLayout
    }

    private fun listCategories(grid: Grid) {
        grid.containerDataSource = BeanItemContainer(Category::class.java, categoryRepo.findAll())
    }

}

data class CategoryItem(var id: Int?, var name: String) {
    override fun toString() = name
}

class CategoryEditor(var categoryRepository: CategoryRepository, var onChange: () -> Unit) : VerticalLayout() {
    var categoryName = TextField("Category name")
    var categoryDescription = TextField("Category description")
    var saveButton = Button("Save", FontAwesome.SAVE)
    var parentCategoryBox = ComboBox("Parent category")

    fun setDatasource() {
        parentCategoryBox.containerDataSource = BeanItemContainer(CategoryItem::class.java, categoryRepository.findAll().map { CategoryItem(it.id, it.name) })
    }

    fun changeCurrentCategory(category: Category) {
        categoryName.value = category.name
        categoryDescription.value = category.description
        parentCategoryBox.value = CategoryItem(category.parentCategory?.id, category.parentCategory?.name?:"")
    }

    init {
        isSpacing = true
        addComponents(categoryName, categoryDescription, parentCategoryBox, saveButton)
        setMargin(true)
        setDatasource()
        saveButton.addClickListener {
            val value: CategoryItem? = parentCategoryBox.value as CategoryItem?
            val parentId = value?.id
            val parentCategory = if (parentId != null) categoryRepository.findOne(parentId) else null
            categoryRepository.save(Category(name = categoryName.value, description = categoryDescription.value, parentCategory = parentCategory))
            setDatasource()
            onChange()
        }
    }
}