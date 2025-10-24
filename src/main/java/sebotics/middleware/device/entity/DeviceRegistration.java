package sebotics.middleware.device.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "device_registrations")
public class DeviceRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(name = "serial_number", nullable = false, unique = true, length = 64)
	private String serialNumber;

	@Column(name = "mac_address", nullable = false, unique = true, length = 64)
	private String macAddress;

	@Column(name = "elevator_vendor", nullable = false, length = 128)
	private String elevatorVendor;

	@Column(name = "registered_at", nullable = false, updatable = false)
	private OffsetDateTime registeredAt;

	protected DeviceRegistration() {
		// JPA requirement
	}

	public DeviceRegistration(String serialNumber, String macAddress, String elevatorVendor) {
		this.serialNumber = serialNumber;
		this.macAddress = macAddress;
		this.elevatorVendor = elevatorVendor;
	}

	@PrePersist
	private void onCreate() {
		if (registeredAt == null) {
			registeredAt = OffsetDateTime.now(ZoneOffset.UTC);
		}
	}

	public UUID getId() {
		return id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getElevatorVendor() {
		return elevatorVendor;
	}

	public void setElevatorVendor(String elevatorVendor) {
		this.elevatorVendor = elevatorVendor;
	}

	public OffsetDateTime getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(OffsetDateTime registeredAt) {
		this.registeredAt = registeredAt;
	}
}
