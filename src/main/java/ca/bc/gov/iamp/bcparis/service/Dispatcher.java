package ca.bc.gov.iamp.bcparis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.iamp.bcparis.processor.datagram.DriverProcessor;
import ca.bc.gov.iamp.bcparis.processor.datagram.VehicleProcessor;

public class Dispatcher {

    private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

    private final DriverProcessor driverProcessor;
    private final VehicleProcessor vehicleProcessor;

    public Dispatcher(DriverProcessor driverProcessor, VehicleProcessor vehicleProcessor) {
        this.driverProcessor = driverProcessor;
        this.vehicleProcessor = vehicleProcessor;
    }

    public Object dispatch(String msgFmt, Object message) {
        if (msgFmt != null) {
            if (msgFmt.contains("DSSMTCPC")) {
                System.out.println(">>> Routing to DriverProcessor based on DSSMTCPC in MsgFFmt");
                log.info("Routing to DriverProcessor based on DSSMTCPC in MsgFFmt");
                return driverProcessor.process((ca.bc.gov.iamp.bcparis.model.message.Layer7Message) message);
            } else if (msgFmt.contains("DSSMTCPV") || msgFmt.contains("JISTRN2")) {
                System.out.println(">>> Routing to VehicleProcessor based on DSSMTCPV or JISTRN2 in MsgFFmt");
                log.info("Routing to VehicleProcessor based on DSSMTCPV or JISTRN2 in MsgFFmt");
                return vehicleProcessor.process((ca.bc.gov.iamp.bcparis.model.message.Layer7Message) message);
            }
        }
        log.warn("Message format is null or unrecognized: {}", msgFmt);
        return null;
    }
}