define(['modules/checkout/validatePayment'], function (validatePayment) {

    describe('#validatePayment', function () {

        it('should validate account number', function () {

            var tooShort = {
                accountNumber: '123',
                accountHolderName: '',
                sortCode: null,
                detailsConfirmed: false
            };
            var tooLong = {
                accountNumber: '123456789101112',
                accountHolderName: '',
                sortCode: null,
                detailsConfirmed: false
            };
            var justRightLow = {
                accountNumber: '123456',
                accountHolderName: '',
                sortCode: null,
                detailsConfirmed: false
            };
            var justRightHigh = {
                accountNumber: '1234567890',
                accountHolderName: '',
                sortCode: null,
                detailsConfirmed: false
            };

            expect((validatePayment(tooShort)).accountNumberValid).toBe(false);
            expect((validatePayment(tooLong)).accountNumberValid).toBe(false);
            expect((validatePayment(justRightLow)).accountNumberValid).toBe(true);
            expect((validatePayment(justRightHigh)).accountNumberValid).toBe(true);
        });

        it('should validate account holder name', function () {
            var tooLong = {
                accountNumber: '',
                accountHolderName: 'This name is longer than 18 characters',
                sortCode: null,
                detailsConfirmed: false
            };
            var valid = {
                accountNumber: '',
                accountHolderName: 'Example Name',
                sortCode: null,
                detailsConfirmed: false
            };
            expect((validatePayment(tooLong)).accountHolderNameValid).toBe(false);
            expect((validatePayment(valid)).accountHolderNameValid).toBe(true);
        });

        it('should validate sort code', function () {

            var tooShort = {
                accountNumber: '',
                accountHolderName: '',
                sortCode: '00-00',
                detailsConfirmed: false
            };
            var tooLong = {
                accountNumber: '',
                accountHolderName: '',
                sortCode: '01-01-0100',
                detailsConfirmed: false
            };
            var justRight = {
                accountNumber: '',
                accountHolderName: '',
                sortCode: '01-01-01',
                detailsConfirmed: false
            };

            expect((validatePayment(tooShort)).sortCodeValid).toBe(false);
            expect((validatePayment(tooLong)).sortCodeValid).toBe(false);
            expect((validatePayment(justRight)).sortCodeValid).toBe(true);

        });

        it('should validate all details', function () {

            var valid = validatePayment({
                accountNumber: '12346789',
                accountHolderName: 'Example Name',
                sortCode: '01-01-01',
                detailsConfirmed: true
            });

            var invalid = validatePayment({
                accountNumber: '12346789',
                accountHolderName: 'This name is longer than 18 characters',
                sortCode: '01-01-0100',
                detailsConfirmed: true
            });

            expect(valid.accountNumberValid).toBe(true);
            expect(valid.accountHolderNameValid).toBe(true);
            expect(valid.sortCodeValid).toBe(true);
            expect(valid.detailsConfirmedValid).toBe(true);
            expect(valid.allValid).toBe(true);

            expect(invalid.accountNumberValid).toBe(true);
            expect(invalid.accountHolderNameValid).toBe(false);
            expect(invalid.sortCodeValid).toBe(false);
            expect(invalid.detailsConfirmedValid).toBe(true);
            expect(invalid.allValid).toBe(false);

        });
    });
});
