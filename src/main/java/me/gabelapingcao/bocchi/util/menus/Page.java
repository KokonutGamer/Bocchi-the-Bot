package me.gabelapingcao.bocchi.util.menus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import me.gabelapingcao.bocchi.util.ComponentHandler;
import me.gabelapingcao.bocchi.util.internal.PageImplementation;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.AbstractMessageBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateRequest;
import net.dv8tion.jda.api.utils.messages.MessageData;
import net.dv8tion.jda.internal.utils.Checks;
import net.dv8tion.jda.internal.utils.Helpers;
import net.dv8tion.jda.internal.utils.IOUtil;

public interface Page extends ComponentHandler {

	boolean DEFAULT_TTS = false;

	MessageData getMessageData();

	List<MenuComponent> getComponents();

	static Page.Builder create() {
		return new Page.Builder();
	}

	class Builder extends AbstractMessageBuilder<Page, Builder> implements MessageCreateRequest<Builder> {

		private final List<FileUpload> files = new ArrayList<>(0);
		private boolean tts;

		public Builder() {
		}

		@Override
		public Builder addContent(String content) {
			Checks.notNull(content, "Content");
			Checks.check(
					Helpers.codePointLength(this.content)
							+ Helpers.codePointLength(content) <= Message.MAX_CONTENT_LENGTH,
					"Cannot have content longer than %d characters", Message.MAX_CONTENT_LENGTH);
			this.content.append(content);
			return this;
		}

		@Override
		public Builder addEmbeds(Collection<? extends MessageEmbed> embeds) {
			Checks.noneNull(embeds, "Embeds");
			Checks.check(this.embeds.size() + embeds.size() <= Message.MAX_EMBED_COUNT,
					"Cannot have more than %d embeds", Message.MAX_EMBED_COUNT);
			this.embeds.addAll(embeds);
			return this;
		}

		@Override
		public Builder setComponents(Collection<? extends LayoutComponent> components) {
			Checks.noneNull(components, "ComponentLayouts");
			for (LayoutComponent layout : components) {
				Checks.check(layout.isMessageCompatible(), "Provided component layout is invalid for messages!");
				layout.getComponents().forEach(item -> Checks.check(item instanceof MenuComponent,
						"Provided component layout contains non-menu components!"));
			}
			Checks.check(components.size() <= Message.MAX_COMPONENT_COUNT,
					"Cannot send more than %d component layouts in a message!", Message.MAX_COMPONENT_COUNT);
			this.components.clear();
			this.components.addAll(components);
			return this;
		}

		@Override
		public Builder addComponents(Collection<? extends LayoutComponent> components) {
			Checks.noneNull(components, "ComponentLayouts");
			for (LayoutComponent layout : components) {
				Checks.check(layout.isMessageCompatible(), "Provided component layout is invalid for messages!");
				layout.getComponents().forEach(item -> Checks.check(item instanceof MenuComponent,
						"Provided component layout contains non-menu components!"));
			}
			Checks.check(this.components.size() + components.size() <= Message.MAX_COMPONENT_COUNT,
					"Cannot send more than %d component layouts in a message!", Message.MAX_COMPONENT_COUNT);
			this.components.addAll(components);
			return this;
		}

		@Override
		public Builder setFiles(Collection<? extends FileUpload> files) {
			if (files != null) {
				Checks.noneNull(files, "Files");
			}
			this.files.clear();
			if (files != null) {
				this.files.addAll(files);
			}
			return this;
		}

		@Override
		public List<FileUpload> getAttachments() {
			return Collections.unmodifiableList(files);
		}

		@Override
		public Builder addFiles(Collection<? extends FileUpload> files) {
			Checks.noneNull(files, "Files");
			this.files.addAll(files);
			return this;
		}

		@Override
		public Builder setTTS(boolean tts) {
			this.tts = tts;
			return this;
		}

		@Override
		public boolean isEmpty() {
			return Helpers.isBlank(content) && embeds.isEmpty() && files.isEmpty() && components.isEmpty();
		}

		@Override
		public boolean isValid() {
			return !isEmpty() && embeds.size() <= Message.MAX_EMBED_COUNT
					&& components.size() <= Message.MAX_COMPONENT_COUNT
					&& Helpers.codePointLength(content) <= Message.MAX_CONTENT_LENGTH;
		}

		@Override
		public Page build() {
			// Same as MessageCreateBuilder - Copy to prevent modifying data after building
			String content = this.content.toString().trim();
			List<MessageEmbed> embeds = new ArrayList<>(this.embeds);
			List<FileUpload> files = new ArrayList<>(this.files);
			List<LayoutComponent> components = new ArrayList<>(this.components);

			if (content.isEmpty() && embeds.isEmpty() && files.isEmpty() && components.isEmpty()) {
				throw new IllegalStateException(
						"Cannot build an empty message. You need at least one of content, embeds, components, or files");
			}
			int length = Helpers.codePointLength(content);
			if (length > Message.MAX_CONTENT_LENGTH) {
				throw new IllegalStateException("Message content is too long! Max length is "
						+ Message.MAX_CONTENT_LENGTH + " characters, provided " + length);
			}
			if (embeds.size() > Message.MAX_EMBED_COUNT) {
				throw new IllegalStateException("Connot build message with over " + Message.MAX_EMBED_COUNT
						+ " embeds, provided " + embeds.size());
			}
			if (components.size() > Message.MAX_COMPONENT_COUNT) {
				throw new IllegalStateException();
			}

			return new PageImplementation(content, embeds, files, components, tts);
		}

		public Builder clear() {
			super.clear();
			this.files.clear();
			this.tts = false;
			return this;
		}

		@Override
		public Builder closeFiles() {
			files.forEach(IOUtil::silentClose);
			files.clear();
			return this;
		}

	}

}
