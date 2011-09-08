package com.ontometrics.scraper.extraction;

import static com.ontometrics.scraper.HtmlSample.DetailPage;
import static com.ontometrics.scraper.extraction.HtmlExtractor.html;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import net.htmlparser.jericho.HTMLElementName;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontometrics.scraper.HtmlSample;
import com.ontometrics.scraper.Record;
import com.ontometrics.scraper.ScrapedRecord;

public class DefaultFieldExtractorTest {

	private static final Logger log = LoggerFactory.getLogger(DefaultFieldExtractorTest.class);

	private FieldExtractor<?> fieldExtractor = new DefaultFieldExtractor();

	@Test
	public void extractFieldsAfterTablePairedTags() throws MalformedURLException, IOException {
		List<Field> fields = fieldExtractor.source(html().url(DetailPage.getUrl()))
				.getFields();

		assertThat(fields.size(), is(greaterThan(0)));
		log.debug("fields = {}", fields);

	}

	@Test
	public void extractsFieldsFromULs() {

		List<Field> fields = fieldExtractor.source(html().url(HtmlSample.TableWithULs.getUrl()))
				.getFields();

		assertThat(fields.size(), is(greaterThan(0)));
		Record record = new ScrapedRecord(fields);

		Field fieldFromUL = new ScrapedField("Application deadline(s)", "07/13/2010");
		assertThat(record.getFields()
				.contains(fieldFromUL), is(true));

	}

	@Test
	public void extractsFieldFromSpecificTagOccurrence() {

		List<Field> fields = fieldExtractor.source(html().url(HtmlSample.TableWithULs.getUrl()))
				.field("source", HTMLElementName.H2)
				.getFields();

		assertThat(fields.size(), is(greaterThan(0)));
		Record record = new ScrapedRecord(fields);

		Field fieldFromUL = new ScrapedField("source", "Autonomous Province of Trento");
		assertThat(record.getFields()
				.contains(fieldFromUL), is(true));

	}
}
