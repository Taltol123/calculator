# Text-Based Calculator Application

A Java implementation of a text-based calculator that evaluates assignment expressions with support for different calculator types and I/O methods.


## Usage

### Command Line Arguments

```bash
java App <ioType> <calculatorType> [inputPath] [outputPath]
```

- `ioType`: Type of I/O handler (`file`)
- `calculatorType`: Type of calculator (`assignment`)
- `inputPath`: Input file path (required when `ioType` is `file`)
- `outputPath`: Output file path (required when `ioType` is `file`)

### Examples

#### File Mode
```bash
java App file basic input.txt output.txt
```

### Input Format

Enter expressions one per line. Each line should contain a valid assignment expression. Empty line finishes input.

Example input:
```
i = 0
j = ++i
x = i++ + 5
y = (5 + 3) * 10
i += y
```

Expected output:
```
(i=82,j=1,x=6,y=80)
```

## Building and Testing

### Build the Project
```bash
./gradlew build
```

### Run Tests
```bash
./gradlew test
```

### Run the Application
```bash
./gradlew run --args="file assignment sample_input.txt output.txt"
```

## Requirements

- Java 8 or higher
- Gradle (for building and testing)

## Testing

Run tests with: `./gradlew test`
