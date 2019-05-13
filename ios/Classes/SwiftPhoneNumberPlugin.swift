import Flutter
import PhoneNumberKit

public class SwiftPhoneNumberPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "phone_number", binaryMessenger: registrar.messenger())
        let instance = SwiftPhoneNumberPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch(call.method) {
        case "formatE164": formatE164(call, result: result)
        default:
            result(FlutterMethodNotImplemented)
        }
    }

    private let kit = PhoneNumberKit()


    private func formatE164(_ call: FlutterMethodCall, result: FlutterResult) {
        guard
            let arguments = call.arguments as? [String : Any],
            let phone = arguments["phone"] as? String
            else {
                result(FlutterError(code: "InvalidArgument",
                                    message: "The 'phone' argument is missing.",
                                    details: nil))
                return
        }

        // Try to parse the string to a phone number for a given region.

        // If the parsing is successful, we return a dictionary containing :
        // - the number in the E164 format
        // - the number in the international format
        // - the number formatted as a national number and without the international prefix
        // - the number type (might not be 100% auccurate)

        // If it fails, we return a FlutterError to notify that the number is invalid.
        do {
            var phoneNumber: PhoneNumber
            print(Locale.current.regionCode);
            if let region = Locale.current.regionCode as? String {
                phoneNumber = try kit.parse(phone, withRegion: region)
            }
            else {
                phoneNumber = try kit.parse(phone)
            }

            result(kit.format(phoneNumber, toType: .e164))
        } catch {
            result(FlutterError(code: "InvalidNumber",
                                message:"Failed to parse phone number string '\(phone)'.",
                                details: nil))
        }
    }

}