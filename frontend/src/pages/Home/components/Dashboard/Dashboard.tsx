import {Card} from "antd";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    BarChart,
    Bar,
    ResponsiveContainer,
} from "recharts";

const lineData = [
    {name: "A", value: 10},
    {name: "B", value: 20},
    {name: "C", value: 50},
    {name: "D", value: 35},
    {name: "E", value: 70},
    {name: "F", value: 25},
    {name: "G", value: 15},
    {name: "H", value: 35},
    {name: "I", value: 25},
    {name: "J", value: 40},
    {name: "K", value: 50},
];

const barData = [
    {name: "Exemplo 1", value: 40},
    {name: "Exemplo 2", value: 20},
    {name: "Exemplo 3", value: 60},
    {name: "Exemplo 4", value: 35},
    {name: "Exemplo 5", value: 22},
    {name: "Exemplo 6", value: 55},
];

export default function Dashboard() {
    return (
        <div className="p-4 sm:p-6 space-y-6">
            <div className="flex flex-col md:flex-row gap-6">
                <Card
                    className="!bg-[#F31E8D] !text-white !rounded-xl !shadow-md w-full md:w-60 text-center flex flex-col justify-center items-center"
                    bodyStyle={{padding: "24px 16px"}}
                >
                    <div className="text-6xl font-bold mb-2">24</div>
                    <p className="text-xl font-semibold leading-4">
                        Média dos incidentes
                        <br/>
                        ocorridos por dia
                    </p>
                </Card>

                <Card
                    className="!bg-gray-100 !rounded-xl !shadow-md w-full min-w-[300px] md:w-[600px]"
                >
                    <div className="h-64">
                        <ResponsiveContainer width="100%" height="100%">
                            <LineChart data={lineData}>
                                <CartesianGrid stroke="#ccc" strokeDasharray="5 5"/>
                                <XAxis dataKey="name"/>
                                <YAxis/>
                                <Tooltip/>
                                <Line
                                    type="monotone"
                                    dataKey="value"
                                    stroke="#F31E8D"
                                    strokeWidth={2}
                                />
                            </LineChart>
                        </ResponsiveContainer>
                    </div>
                </Card>
            </div>

            <Card className="!bg-gray-100 !rounded-xl !shadow-md w-full min-w-[300px]">
                <div className="h-64">
                    <ResponsiveContainer width="100%" height="100%">
                        <BarChart data={barData}>
                            <CartesianGrid strokeDasharray="3 3"/>
                            <XAxis dataKey="name"/>
                            <YAxis/>
                            <Tooltip/>
                            <Bar dataKey="value" fill="#F31E8D"/>
                        </BarChart>
                    </ResponsiveContainer>
                </div>
            </Card>
        </div>
    );
}
