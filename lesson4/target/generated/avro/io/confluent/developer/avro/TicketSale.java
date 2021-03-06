/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package io.confluent.developer.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class TicketSale extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -3074389728640958262L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TicketSale\",\"namespace\":\"io.confluent.developer.avro\",\"fields\":[{\"name\":\"title\",\"type\":\"string\"},{\"name\":\"sale_ts\",\"type\":\"string\"},{\"name\":\"ticket_total_value\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<TicketSale> ENCODER =
      new BinaryMessageEncoder<TicketSale>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<TicketSale> DECODER =
      new BinaryMessageDecoder<TicketSale>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<TicketSale> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<TicketSale> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<TicketSale> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<TicketSale>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this TicketSale to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a TicketSale from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a TicketSale instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static TicketSale fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private java.lang.CharSequence title;
   private java.lang.CharSequence sale_ts;
   private int ticket_total_value;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public TicketSale() {}

  /**
   * All-args constructor.
   * @param title The new value for title
   * @param sale_ts The new value for sale_ts
   * @param ticket_total_value The new value for ticket_total_value
   */
  public TicketSale(java.lang.CharSequence title, java.lang.CharSequence sale_ts, java.lang.Integer ticket_total_value) {
    this.title = title;
    this.sale_ts = sale_ts;
    this.ticket_total_value = ticket_total_value;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return title;
    case 1: return sale_ts;
    case 2: return ticket_total_value;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: title = (java.lang.CharSequence)value$; break;
    case 1: sale_ts = (java.lang.CharSequence)value$; break;
    case 2: ticket_total_value = (java.lang.Integer)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'title' field.
   * @return The value of the 'title' field.
   */
  public java.lang.CharSequence getTitle() {
    return title;
  }


  /**
   * Sets the value of the 'title' field.
   * @param value the value to set.
   */
  public void setTitle(java.lang.CharSequence value) {
    this.title = value;
  }

  /**
   * Gets the value of the 'sale_ts' field.
   * @return The value of the 'sale_ts' field.
   */
  public java.lang.CharSequence getSaleTs() {
    return sale_ts;
  }


  /**
   * Sets the value of the 'sale_ts' field.
   * @param value the value to set.
   */
  public void setSaleTs(java.lang.CharSequence value) {
    this.sale_ts = value;
  }

  /**
   * Gets the value of the 'ticket_total_value' field.
   * @return The value of the 'ticket_total_value' field.
   */
  public int getTicketTotalValue() {
    return ticket_total_value;
  }


  /**
   * Sets the value of the 'ticket_total_value' field.
   * @param value the value to set.
   */
  public void setTicketTotalValue(int value) {
    this.ticket_total_value = value;
  }

  /**
   * Creates a new TicketSale RecordBuilder.
   * @return A new TicketSale RecordBuilder
   */
  public static io.confluent.developer.avro.TicketSale.Builder newBuilder() {
    return new io.confluent.developer.avro.TicketSale.Builder();
  }

  /**
   * Creates a new TicketSale RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new TicketSale RecordBuilder
   */
  public static io.confluent.developer.avro.TicketSale.Builder newBuilder(io.confluent.developer.avro.TicketSale.Builder other) {
    if (other == null) {
      return new io.confluent.developer.avro.TicketSale.Builder();
    } else {
      return new io.confluent.developer.avro.TicketSale.Builder(other);
    }
  }

  /**
   * Creates a new TicketSale RecordBuilder by copying an existing TicketSale instance.
   * @param other The existing instance to copy.
   * @return A new TicketSale RecordBuilder
   */
  public static io.confluent.developer.avro.TicketSale.Builder newBuilder(io.confluent.developer.avro.TicketSale other) {
    if (other == null) {
      return new io.confluent.developer.avro.TicketSale.Builder();
    } else {
      return new io.confluent.developer.avro.TicketSale.Builder(other);
    }
  }

  /**
   * RecordBuilder for TicketSale instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TicketSale>
    implements org.apache.avro.data.RecordBuilder<TicketSale> {

    private java.lang.CharSequence title;
    private java.lang.CharSequence sale_ts;
    private int ticket_total_value;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(io.confluent.developer.avro.TicketSale.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.title)) {
        this.title = data().deepCopy(fields()[0].schema(), other.title);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.sale_ts)) {
        this.sale_ts = data().deepCopy(fields()[1].schema(), other.sale_ts);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.ticket_total_value)) {
        this.ticket_total_value = data().deepCopy(fields()[2].schema(), other.ticket_total_value);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
    }

    /**
     * Creates a Builder by copying an existing TicketSale instance
     * @param other The existing instance to copy.
     */
    private Builder(io.confluent.developer.avro.TicketSale other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.title)) {
        this.title = data().deepCopy(fields()[0].schema(), other.title);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.sale_ts)) {
        this.sale_ts = data().deepCopy(fields()[1].schema(), other.sale_ts);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.ticket_total_value)) {
        this.ticket_total_value = data().deepCopy(fields()[2].schema(), other.ticket_total_value);
        fieldSetFlags()[2] = true;
      }
    }

    /**
      * Gets the value of the 'title' field.
      * @return The value.
      */
    public java.lang.CharSequence getTitle() {
      return title;
    }


    /**
      * Sets the value of the 'title' field.
      * @param value The value of 'title'.
      * @return This builder.
      */
    public io.confluent.developer.avro.TicketSale.Builder setTitle(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.title = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'title' field has been set.
      * @return True if the 'title' field has been set, false otherwise.
      */
    public boolean hasTitle() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'title' field.
      * @return This builder.
      */
    public io.confluent.developer.avro.TicketSale.Builder clearTitle() {
      title = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'sale_ts' field.
      * @return The value.
      */
    public java.lang.CharSequence getSaleTs() {
      return sale_ts;
    }


    /**
      * Sets the value of the 'sale_ts' field.
      * @param value The value of 'sale_ts'.
      * @return This builder.
      */
    public io.confluent.developer.avro.TicketSale.Builder setSaleTs(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.sale_ts = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'sale_ts' field has been set.
      * @return True if the 'sale_ts' field has been set, false otherwise.
      */
    public boolean hasSaleTs() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'sale_ts' field.
      * @return This builder.
      */
    public io.confluent.developer.avro.TicketSale.Builder clearSaleTs() {
      sale_ts = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'ticket_total_value' field.
      * @return The value.
      */
    public int getTicketTotalValue() {
      return ticket_total_value;
    }


    /**
      * Sets the value of the 'ticket_total_value' field.
      * @param value The value of 'ticket_total_value'.
      * @return This builder.
      */
    public io.confluent.developer.avro.TicketSale.Builder setTicketTotalValue(int value) {
      validate(fields()[2], value);
      this.ticket_total_value = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'ticket_total_value' field has been set.
      * @return True if the 'ticket_total_value' field has been set, false otherwise.
      */
    public boolean hasTicketTotalValue() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'ticket_total_value' field.
      * @return This builder.
      */
    public io.confluent.developer.avro.TicketSale.Builder clearTicketTotalValue() {
      fieldSetFlags()[2] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TicketSale build() {
      try {
        TicketSale record = new TicketSale();
        record.title = fieldSetFlags()[0] ? this.title : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.sale_ts = fieldSetFlags()[1] ? this.sale_ts : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.ticket_total_value = fieldSetFlags()[2] ? this.ticket_total_value : (java.lang.Integer) defaultValue(fields()[2]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<TicketSale>
    WRITER$ = (org.apache.avro.io.DatumWriter<TicketSale>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<TicketSale>
    READER$ = (org.apache.avro.io.DatumReader<TicketSale>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.title);

    out.writeString(this.sale_ts);

    out.writeInt(this.ticket_total_value);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.title = in.readString(this.title instanceof Utf8 ? (Utf8)this.title : null);

      this.sale_ts = in.readString(this.sale_ts instanceof Utf8 ? (Utf8)this.sale_ts : null);

      this.ticket_total_value = in.readInt();

    } else {
      for (int i = 0; i < 3; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.title = in.readString(this.title instanceof Utf8 ? (Utf8)this.title : null);
          break;

        case 1:
          this.sale_ts = in.readString(this.sale_ts instanceof Utf8 ? (Utf8)this.sale_ts : null);
          break;

        case 2:
          this.ticket_total_value = in.readInt();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










