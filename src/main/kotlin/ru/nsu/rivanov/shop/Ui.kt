package ru.nsu.rivanov.shop

import com.vaadin.annotations.Theme
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.server.VaadinRequest
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.Grid
import com.vaadin.ui.TextField
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import org.springframework.beans.factory.annotation.Autowired




@SpringUI(path = "/ui")
@Theme("valo")
class VaadinUI : UI() {
    @Autowired lateinit var categoryRepository: CategoryRepository
    var grid = Grid()

    private val filterText = TextField()


    override fun init(request: VaadinRequest) {
        val layout = VerticalLayout()


        filterText.inputPrompt = "filter by name..."
        filterText.addTextChangeListener { e ->
            grid.containerDataSource = BeanItemContainer<Category>(Category::class.java,
                    categoryRepository.findAll().filter { it.name.toLowerCase().startsWith(e.text.toLowerCase()) })
        }
        layout.addComponent(filterText)

        grid.containerDataSource = BeanItemContainer<Category>(Category::class.java, categoryRepository.findAll())
        layout.addComponent(grid)
        layout.margin = MarginInfo(true)
        content = layout
    }

}