package com.fastaccess.github.di.modules

import android.annotation.SuppressLint
import android.content.Context
import com.fastaccess.data.storage.FastHubSharedPreference
import com.fastaccess.github.R
import com.fastaccess.github.base.engine.ThemeEngine
import com.fastaccess.github.di.annotations.ForApplication
import com.fastaccess.github.di.scopes.PerFragment
import com.fastaccess.github.platform.mentions.MentionsPresenter
import com.fastaccess.github.ui.modules.issue.fragment.IssueFragment
import com.fastaccess.github.usecase.search.FilterSearchUsersUseCase
import com.fastaccess.github.utils.extensions.theme
import com.fastaccess.markdown.GrammarLocatorDef
import com.fastaccess.markdown.extension.markwon.emoji.EmojiPlugin
import dagger.Module
import dagger.Provides
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.Prism4jThemeDefault
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j

/**
 * Created by Kosh on 02.02.19.
 */
@Module
class FragmentModule {

    @PerFragment @Provides fun provideContext(fragment: IssueFragment) = fragment.requireContext()

    @SuppressLint("PrivateResource")
    @PerFragment @Provides fun provideMarkwon(
        @ForApplication context: Context,
        preference: FastHubSharedPreference
    ): Markwon = Markwon.builder(context)
        .usePlugin(JLatexMathPlugin.create(context.resources.getDimension(R.dimen.abc_text_size_subhead_material)))
        .usePlugin(TaskListPlugin.create(context))
        .usePlugin(HtmlPlugin.create())
        .usePlugin(GlideImagesPlugin.create(context))
        .usePlugin(TablePlugin.create(context))
        .usePlugin(StrikethroughPlugin.create())
        .usePlugin(LinkifyPlugin.create())
        .usePlugin(
            SyntaxHighlightPlugin.create(
                Prism4j(GrammarLocatorDef()), if (ThemeEngine.isLightTheme(preference.theme)) {
                    Prism4jThemeDefault.create()
                } else {
                    Prism4jThemeDarkula.create()
                }
            )
        )
        .usePlugin(EmojiPlugin.create())
        .build()

    @PerFragment @Provides fun provideMentionsPresenter(
        context: Context,
        searchUsersUseCase: FilterSearchUsersUseCase
    ) = MentionsPresenter(context, searchUsersUseCase)
}