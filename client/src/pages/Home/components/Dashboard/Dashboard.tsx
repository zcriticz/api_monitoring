import { Card } from "antd";
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
  { name: "2023-01-01", value: 1 },
  { name: "2023-01-02", value: 1 },
  { name: "2023-01-03", value: 1 },
  { name: "2023-01-04", value: 1 },
  { name: "2023-01-05", value: 1 },
  { name: "2023-01-06", value: 2 },
];

const barData = [
  { name: "Cliente A", value: 1 },
  { name: "Cliente B", value: 1 },
  { name: "Cliente C", value: 1 },
  { name: "Cliente D", value: 1 },
  { name: "Cliente E", value: 1 },
  { name: "Cliente F", value: 1 },
  { name: "Cliente G", value: 1 },
];

export default function Dashboard() {
  return (
    <div className="p-4 sm:p-6 space-y-6">
      <div className="flex flex-col md:flex-row gap-6">
        <Card
          className="!bg-[#F31E8D] !text-white !rounded-xl !shadow-md w-full md:w-60 text-center flex flex-col justify-center items-center"
          bodyStyle={{ padding: "24px 16px" }}
        >
          <div className="text-6xl font-bold mb-2">1.17</div>
          <p className="text-xl font-semibold leading-4">
            Média dos incidentes
            <br />
            ocorridos por dia
          </p>
        </Card>

        <Card className="!bg-gray-100 !rounded-xl !shadow-md w-full min-w-[300px] md:w-[600px]">
          <div className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={lineData}>
                <CartesianGrid stroke="#ccc" strokeDasharray="5 5" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
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
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="value" fill="#F31E8D" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </Card>
    </div>
  );
}
